// Simple scroll-reveal for cards/sections + mobile nav toggle
document.addEventListener("DOMContentLoaded", function () {

  // Highlight session state in navbar (basic client-side check via a
  // data attribute the page sets after checking session server-side,
  // or just left as-is for static pages).
  const revealEls = document.querySelectorAll(".reveal");
  if ("IntersectionObserver" in window && revealEls.length) {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("in-view");
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.15 });

    revealEls.forEach((el) => observer.observe(el));
  }
});
