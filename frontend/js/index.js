'use strict';

let alleBoeken = [];

async function haalBoekenOp() {
    try {
        const response = await fetch(URL + "/boeken");
        const data = await response.json();

        alleBoeken = data;

        vulGenres();
        toonBoeken(alleBoeken);
    } catch (fout) {
        const container = document.getElementById("resultaten");
        container.innerHTML = "<div class='melding-fout'>Kan geen verbinding maken met de backend op poort 8080.</div>";
    }
}

function toonBoeken(items) {
    const container = document.getElementById("resultaten");
    container.innerHTML = "";

    if (items.length === 0) {
        container.innerHTML = "<p>Geen boeken gevonden!</p>";
        return;
    }

    for (let boek of items) {
        const kaart = document.createElement("div");
        kaart.classList.add("card");

        let opVoorraad = boek.stock > 0;
        let voorraadHtml = opVoorraad
            ? `<span class="groen">Op voorraad (${boek.stock})</span>`
            : `<span class="rood">Uitverkocht</span>`;

        kaart.innerHTML = `
            <div>
                <h3>${boek.titel}</h3>
                <p><strong>Auteur:</strong> ${boek.auteurs}</p>
                <p><strong>Genre:</strong> ${boek.genre}</p>
                <p><strong>Prijs:</strong> € ${boek.prijs.toFixed(2)}</p>
                <p>${voorraadHtml}</p>
            </div>
            <div>
                <div class="aantal-rij">
                    <label>Aantal:</label>
                    <input type="number" class="aantal-input" min="1" max="${boek.stock}" value="1" ${!opVoorraad ? "disabled" : ""}>
                </div>
                <button class="details licht">Details & Reviews</button>
                <button class="reserveer" ${!opVoorraad ? "disabled" : ""}>Reserveer</button>
            </div>
        `;

        kaart.querySelector(".details").onclick = () => {
            localStorage.setItem("gekozenBoek", JSON.stringify(boek));
            window.location.href = "boek.html";
        };

        kaart.querySelector(".reserveer").onclick = () => {
            const gekozenAantal = kaart.querySelector(".aantal-input").value;
            voegToeAanMandje(boek, gekozenAantal);
        };

        container.appendChild(kaart);
    }
}

function vulGenres() {
    const genreSelect = document.getElementById("genreFilter");
    let genres = [];

    for (let boek of alleBoeken) {
        if (boek.genre && !genres.includes(boek.genre)) {
            genres.push(boek.genre);
        }
    }

    genres.sort().forEach(g => {
        let opt = document.createElement("option");
        opt.value = g;
        opt.textContent = g;
        genreSelect.appendChild(opt);
    });
}

function filterEnZoek() {
    const zoekTerm = document.getElementById("zoekbalk").value.toLowerCase();
    const gekozenGenre = document.getElementById("genreFilter").value;

    const resultaat = alleBoeken.filter(boek => {
        let titel = (boek.titel || "").toLowerCase();
        let auteur = (boek.auteurs || "").toLowerCase();
        let genre = boek.genre || "";

        let matchZoek = titel.includes(zoekTerm) || auteur.includes(zoekTerm);
        let matchGenre = gekozenGenre === "" || genre === gekozenGenre;

        return matchZoek && matchGenre;
    });

    toonBoeken(resultaat);
}

window.onload = () => {
    toonNavigatie();
    haalBoekenOp();

    const zoekbalk = document.getElementById("zoekbalk");
    if (zoekbalk) zoekbalk.oninput = filterEnZoek;

    const genreSelect = document.getElementById("genreFilter");
    if (genreSelect) genreSelect.onchange = filterEnZoek;
};