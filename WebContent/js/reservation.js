const TIME_SLOTS = ["12:00", "13:00", "14:00", "19:00", "20:00", "21:00"];

let selectedTime = null;
let selectedTableId = null;

function initDatePicker() {
  const dateInput = document.getElementById("resDate");
  if (!dateInput) return;

  const today = new Date();
  const yyyy = today.getFullYear();
  const mm = String(today.getMonth() + 1).padStart(2, "0");
  const dd = String(today.getDate()).padStart(2, "0");
  const todayStr = `${yyyy}-${mm}-${dd}`;

  dateInput.min = todayStr;
  dateInput.value = todayStr;
}

function renderTimeSlots() {
  const wrap = document.getElementById("timeSlots");
  if (!wrap) return;

  wrap.innerHTML = "";
  TIME_SLOTS.forEach((slot) => {
    const btn = document.createElement("div");
    btn.className = "slot-btn";
    btn.textContent = formatTime(slot);
    btn.dataset.time = slot;
    btn.addEventListener("click", () => {
      document.querySelectorAll(".slot-btn").forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");
      selectedTime = slot;
      selectedTableId = null;
      loadTables();
    });
    wrap.appendChild(btn);
  });
}

function formatTime(t) {
  const [h, m] = t.split(":").map(Number);
  const period = h >= 12 ? "PM" : "AM";
  const hour12 = h % 12 === 0 ? 12 : h % 12;
  return `${hour12}:${String(m).padStart(2, "0")} ${period}`;
}

function loadTables() {
  const dateInput = document.getElementById("resDate");
  const floor = document.getElementById("tableFloor");
  const summary = document.getElementById("bookingSummary");

  if (!dateInput.value || !selectedTime) {
    floor.innerHTML = '<p style="color:#8A7F75;font-size:13.5px;">Pick a date and time slot above to see available tables.</p>';
    return;
  }

  floor.innerHTML = '<p style="color:#8A7F75;font-size:13.5px;">Loading tables...</p>';

  fetch(`tables?date=${encodeURIComponent(dateInput.value)}&time=${encodeURIComponent(selectedTime)}`)
    .then((res) => res.json())
    .then((tables) => renderTables(tables))
    .catch(() => {
      floor.innerHTML = '<p style="color:#B33A3A;font-size:13.5px;">Could not load tables. Is the server running?</p>';
    });
}

function renderTables(tables) {
  const floor = document.getElementById("tableFloor");
  floor.innerHTML = "";

  if (!tables.length) {
    floor.innerHTML = '<p style="color:#8A7F75;font-size:13.5px;">No tables configured yet.</p>';
    return;
  }

  tables.forEach((t) => {
    const div = document.createElement("div");
    div.className = "table-item" + (t.available ? "" : " booked");
    if (selectedTableId === t.tableId) div.classList.add("selected");

    div.innerHTML = `
      <span class="t-icon">🍽️</span>
      <span>${t.tableNo}</span>
      <span style="font-weight:400;">${t.capacity} seats</span>
    `;

    if (t.available) {
      div.addEventListener("click", () => {
        selectedTableId = t.tableId;
        document.getElementById("selectedTableId").value = t.tableId;
        renderTables(tables);
        updateSummary(t);
      });
    }

    floor.appendChild(div);
  });
}

function updateSummary(table) {
  const summary = document.getElementById("bookingSummary");
  const date = document.getElementById("resDate").value;
  summary.style.display = "block";
  summary.innerHTML = `You're booking <strong>${table.tableNo}</strong> (${table.capacity} seats, ${table.location})
    on <strong>${date}</strong> at <strong>${formatTime(selectedTime)}</strong>.`;
}

function handleReservationForm() {
  const form = document.getElementById("reservationForm");
  if (!form) return;

  const alertBox = document.getElementById("reservationAlert");

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    if (!selectedTableId) {
      showAlert(alertBox, "Please choose a table first.", "error");
      return;
    }

    const formData = new FormData(form);
    formData.set("date", document.getElementById("resDate").value);
    formData.set("time", selectedTime);
    formData.set("tableId", selectedTableId);

    fetch("reserve", { method: "POST", body: formData })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          showAlert(alertBox, data.message, "success");
          form.reset();
          selectedTableId = null;
          document.getElementById("bookingSummary").style.display = "none";
          loadTables();
        } else {
          showAlert(alertBox, data.message, "error");
        }
      })
      .catch(() => showAlert(alertBox, "Could not reach the server. Please try again.", "error"));
  });
}

document.addEventListener("DOMContentLoaded", function () {
  initDatePicker();
  renderTimeSlots();
  handleReservationForm();

  document.getElementById("resDate").addEventListener("change", () => {
    selectedTableId = null;
    loadTables();
  });
});
