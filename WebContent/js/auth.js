function showAlert(box, message, type) {
  box.textContent = message;
  box.className = "alert alert-" + type;
  box.style.display = "block";
}

function handleLoginForm() {
  const form = document.getElementById("loginForm");
  if (!form) return;

  const alertBox = document.getElementById("loginAlert");

  form.addEventListener("submit", function (e) {
    e.preventDefault();
    const formData = new FormData(form);

    fetch("login", { method: "POST", body: formData })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          showAlert(alertBox, "Welcome back, " + data.name + "! Redirecting...", "success");
          setTimeout(() => { window.location.href = "reservation.html"; }, 900);
        } else {
          showAlert(alertBox, data.message || "Login failed.", "error");
        }
      })
      .catch(() => showAlert(alertBox, "Could not reach the server. Please try again.", "error"));
  });
}

function handleRegisterForm() {
  const form = document.getElementById("registerForm");
  if (!form) return;

  const alertBox = document.getElementById("registerAlert");

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const password = form.querySelector("[name='password']").value;
    const confirm = form.querySelector("[name='confirmPassword']").value;

    if (password !== confirm) {
      showAlert(alertBox, "Passwords do not match.", "error");
      return;
    }

    const formData = new FormData(form);

    fetch("register", { method: "POST", body: formData })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          showAlert(alertBox, data.message, "success");
          form.reset();
          setTimeout(() => { window.location.href = "login.html"; }, 1200);
        } else {
          showAlert(alertBox, data.message || "Registration failed.", "error");
        }
      })
      .catch(() => showAlert(alertBox, "Could not reach the server. Please try again.", "error"));
  });
}

document.addEventListener("DOMContentLoaded", function () {
  handleLoginForm();
  handleRegisterForm();
});
