// ==========================================
// SMARTTRANSIT - ADMIN LOGIN
// ==========================================

// Demo admin credentials
const ADMIN_USERNAME = "admin";
const ADMIN_PASSWORD = "admin123";


// ==========================================
// GET LOGIN FORM ELEMENTS
// ==========================================

const loginForm = document.getElementById("adminLoginForm");
const usernameInput = document.getElementById("adminUsername");
const passwordInput = document.getElementById("adminPassword");
const loginError = document.getElementById("loginError");


// ==========================================
// LOGIN FORM SUBMISSION
// ==========================================

loginForm.addEventListener("submit", function (event) {

    // Prevent page refresh
    event.preventDefault();

    // Get entered values
    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();


    // Hide previous error
    loginError.classList.add("d-none");


    // ==========================================
    // CHECK LOGIN DETAILS
    // ==========================================

    if (
        username === ADMIN_USERNAME &&
        password === ADMIN_PASSWORD
    ) {

        // Save login status
        sessionStorage.setItem("adminLoggedIn", "true");

        // Save admin username
        sessionStorage.setItem("adminUsername", username);


        // Redirect to dashboard
        window.location.href = "admin-dashboard.html";

    } else {

        // Show error message
        loginError.textContent =
            "Invalid username or password.";

        loginError.classList.remove("d-none");

        // Clear password
        passwordInput.value = "";

        // Focus password field
        passwordInput.focus();
    }

});