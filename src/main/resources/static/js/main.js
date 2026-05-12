document.addEventListener("DOMContentLoaded", () => {
    const navbarToggle = document.getElementById("navbarToggle");
    const navbarLinks = document.getElementById("navbarLinks");

    if (navbarToggle && navbarLinks) {
        navbarToggle.addEventListener("click", () => {
            const isOpen = navbarLinks.classList.toggle("active");
            navbarToggle.setAttribute("aria-expanded", String(isOpen));
        });
    }
});