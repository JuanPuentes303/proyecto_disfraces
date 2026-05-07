const navbarToggle = document.getElementById("navbarToggle");
const navbarLinks = document.getElementById("navbarLinks");

if (navbarToggle && navbarLinks) {
    navbarToggle.addEventListener("click", () => {
        navbarLinks.classList.toggle("active");
    });
}