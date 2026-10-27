'use strict';

let gekozenBoek = JSON.parse(localStorage.getItem("gekozenBoek"));

async function haalBoekDetailsOp() {
    if (!gekozenBoek) {
        window.location.href = "index.html";
        return;
    }

    try {
        const response = await fetch(URL + "/boeken/" + gekozenBoek.isbn);
        const boek = await response.json();
        gekozenBoek = boek;

        const container = document.getElementById("boek-info");
        let opVoorraad = boek.stock > 0;
        let voorraadHtml = opVoorraad
            ? `<span class="groen">Op voorraad (${boek.stock})</span>`
            : `<span class="rood">Uitverkocht</span>`;

        container.innerHTML = `
            <h2>${boek.titel}</h2>
            <p><strong>Auteur:</strong> ${boek.auteurs}</p>
            <p><strong>ISBN:</strong> ${boek.isbn}</p>
            <p><strong>Genre:</strong> ${boek.genre}</p>
            <p><strong>Prijs:</strong> € ${boek.prijs.toFixed(2)}</p>
            <p>${voorraadHtml}</p>
            <p><strong>Beschrijving:</strong> ${boek.descriptie}</p>
            <div class="aantal-rij">
                <label>Aantal:</label>
                <input type="number" id="detail-aantal" class="aantal-input" min="1" max="${boek.stock}" value="1" ${!opVoorraad ? "disabled" : ""}>
                <button id="boek-reserveer" ${!opVoorraad ? "disabled" : ""}>Voeg toe aan mandje</button>
            </div>
        `;

        document.getElementById("boek-reserveer").onclick = () => {
            const aantal = document.getElementById("detail-aantal").value;
            voegToeAanMandje(boek, aantal);
        };
    } catch (fout) {
        console.error("Fout bij ophalen boek:", fout);
    }
}

async function haalReviewsOp() {
    if (!gekozenBoek) return;

    try {
        const response = await fetch(URL + "/reviews/boek/" + gekozenBoek.isbn);
        const reviews = await response.json();
        reviews.reverse();

        const container = document.getElementById("reviews-lijst");
        container.innerHTML = "";

        if (reviews.length === 0) {
            container.innerHTML = "<p>Er zijn nog geen reviews voor dit boek.</p>";
            return;
        }

        for (let r of reviews) {
            const kaart = document.createElement("div");
            kaart.classList.add("card", "breed");
            kaart.innerHTML = `
                <p><strong>Gebruiker:</strong> ${r.gebruikersnaam}</p>
                <p class="klein"><strong>Geplaatst op:</strong> ${r.datum || "vandaag"}</p>
                <p><span class="sterren">${maakSterren(r.score)}</span></p>
                <p>${r.tekst}</p>
            `;
            container.appendChild(kaart);
        }
    } catch (fout) {
        console.error("Fout bij ophalen reviews:", fout);
    }
}

window.onload = () => {
    toonNavigatie();
    haalBoekDetailsOp();
    haalReviewsOp();
};