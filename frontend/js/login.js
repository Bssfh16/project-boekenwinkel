'use strict';

async function logIn() {
    const melding = document.getElementById("auth-melding");
    const naam = document.getElementById("login-naam").value.trim();
    const ww = document.getElementById("login-ww").value;

    if (naam === "" || ww === "") {
        melding.innerHTML = "<div class='melding-fout'>Vul je gebruikersnaam en wachtwoord in.</div>";
        return;
    }

    try {
        const response = await fetch(URL + "/gebruikers/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                gebruikersnaam: naam,
                wachtwoord: ww
            })
        });

        if (response.ok) {
            const profiel = await response.json();
            localStorage.setItem("gebruiker", JSON.stringify(profiel));
            window.location.href = "profiel.html";
        } else {
            melding.innerHTML = "<div class='melding-fout'>Foute gebruikersnaam of wachtwoord.</div>";
        }
    } catch (fout) {
        melding.innerHTML = "<div class='melding-fout'>Kan geen verbinding maken met de backend.</div>";
    }
}

async function registreer() {
    const melding = document.getElementById("auth-melding");
    const naam = document.getElementById("reg-naam").value.trim();
    const email = document.getElementById("reg-email").value.trim();
    const ww = document.getElementById("reg-ww").value;

    if (naam === "" || email === "" || ww === "") {
        melding.innerHTML = "<div class='melding-fout'>Vul alle velden in om te registreren.</div>";
        return;
    }

    try {
        const response = await fetch(URL + "/gebruikers/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                gebruikersnaam: naam,
                email: email,
                wachtwoord: ww
            })
        });

        if (response.ok) {
            const profiel = await response.json();
            localStorage.setItem("gebruiker", JSON.stringify(profiel));
            window.location.href = "profiel.html";
        } else {
            melding.innerHTML = "<div class='melding-fout'>Registratie mislukt: gebruikersnaam of e-mail bestaat al (of ongeldig e-mailformaat).</div>";
        }
    } catch (fout) {
        melding.innerHTML = "<div class='melding-fout'>Kan geen verbinding maken met de backend.</div>";
    }
}

window.onload = () => {
    toonNavigatie();

    const loginKnop = document.getElementById("login-knop");
    if (loginKnop) loginKnop.onclick = logIn;

    const registerKnop = document.getElementById("register-knop");
    if (registerKnop) registerKnop.onclick = registreer;
};