'use strict';

const URL = "http://localhost:8080";

function maakSterren(score) {
    let sterren = "";
    for (let i = 1; i <= 5; i++) {
        if (i <= score) {
            sterren += "★";
        } else {
            sterren += "☆";
        }
    }
    return sterren;
}

function telTotaalMandje() {
    let mandje = JSON.parse(localStorage.getItem("mandje")) || [];
    let totaalAantal = 0;
    for (let item of mandje) {
        totaalAantal += item.aantal;
    }
    return totaalAantal;
}

function toonNavigatie() {
    const navContainer = document.getElementById("nav-links");
    if (!navContainer) return;

    let gebruiker = JSON.parse(localStorage.getItem("gebruiker"));
    let aantalInMandje = telTotaalMandje();

    if (gebruiker) {
        navContainer.innerHTML = `
            <a href="index.html">Catalogus</a>
            <a href="profiel.html">Profiel & Mandje (${aantalInMandje})</a>
            <a href="#" id="uitlog-knop">Uitloggen (${gebruiker.gebruikersnaam})</a>
        `;

        document.getElementById("uitlog-knop").onclick = () => {
            localStorage.removeItem("gebruiker");
            localStorage.removeItem("mandje");
            window.location.href = "index.html";
        };
    } else {
        navContainer.innerHTML = `
            <a href="index.html">Catalogus</a>
            <a href="login.html">Inloggen</a>
        `;
    }
}

function voegToeAanMandje(boek, gekozenAantal) {
    let gebruiker = JSON.parse(localStorage.getItem("gebruiker"));

    if (!gebruiker) {
        alert("Log eerst in om een boek te reserveren!");
        window.location.href = "login.html";
        return;
    }

    let aantal = Number(gekozenAantal);
    if (!aantal || aantal < 1) {
        alert("Kies een geldig aantal (minstens 1).");
        return;
    }

    if (boek.stock < 1) {
        alert("Dit boek is momenteel uitverkocht!");
        return;
    }

    let mandje = JSON.parse(localStorage.getItem("mandje")) || [];
    let gevondenItem = null;

    for (let opgeslagen of mandje) {
        if (opgeslagen.isbn === boek.isbn) {
            gevondenItem = opgeslagen;
        }
    }

    let nieuwTotaal = aantal;
    if (gevondenItem) {
        nieuwTotaal = gevondenItem.aantal + aantal;
    }

    if (nieuwTotaal > boek.stock) {
        alert("Er zijn maar " + boek.stock + " exemplaren op voorraad!");
        return;
    }

    if (gevondenItem) {
        gevondenItem.aantal = nieuwTotaal;
    } else {
        mandje.push({
            isbn: boek.isbn,
            titel: boek.titel,
            auteurs: boek.auteurs,
            prijs: boek.prijs,
            stock: boek.stock,
            aantal: aantal
        });
    }

    localStorage.setItem("mandje", JSON.stringify(mandje));
    toonNavigatie();
    alert(aantal + "x '" + boek.titel + "' toegevoegd aan je mandje!");
}