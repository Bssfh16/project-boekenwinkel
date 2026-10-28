'use strict';

let gebruiker = JSON.parse(localStorage.getItem("gebruiker"));
let mijnReviews = [];

async function haalProfielOp() {
    try {
        const response = await fetch(URL + "/gebruikers/" + gebruiker.id + "/profiel");
        const data = await response.json();

        localStorage.setItem("gebruiker", JSON.stringify(data));
        gebruiker = data;

        const container = document.getElementById("profiel-gegevens");
        container.innerHTML = `
            <p><strong>Gebruikersnaam:</strong> ${data.gebruikersnaam}</p>
            <p><strong>E-mail:</strong> ${data.email}</p>
        `;
    } catch (fout) {
        console.error("Fout bij het ophalen van de profiel:", fout);
    }
}

function toonMandje() {
    const container = document.getElementById("mandje-container");
    const reserveerKnop = document.getElementById("reserveer-alles");
    let mandje = JSON.parse(localStorage.getItem("mandje")) || [];

    container.innerHTML = "";

    if (mandje.length === 0) {
        container.innerHTML = "<p>Jouw winkelmandje is leeg.</p>";
        reserveerKnop.style.display = "none";
        return;
    }

    reserveerKnop.style.display = "inline-block";
    let totaalPrijs = 0;

    for (let i = 0; i < mandje.length; i++) {
        let item = mandje[i];
        let subtotaal = item.prijs * item.aantal;
        totaalPrijs += subtotaal;

        const rij = document.createElement("div");
        rij.classList.add("mandje-rij");

        rij.innerHTML = `
            <span><strong>${item.titel}</strong> (€ ${item.prijs.toFixed(2)} / stuk)</span>
            <div class="aantal-rij">
                <input type="number" class="aantal-input" min="1" max="${item.stock}" value="${item.aantal}">
                <span>= € ${subtotaal.toFixed(2)}</span>
                <button class="verwijder licht">Verwijderen</button>
            </div>
        `;

        rij.querySelector(".aantal-input").onchange = (e) => {
            let nieuwAantal = Number(e.target.value);
            if (nieuwAantal < 1) nieuwAantal = 1;
            if (nieuwAantal > item.stock) {
                alert("Maximaal " + item.stock + " op voorraad!");
                nieuwAantal = item.stock;
            }
            mandje[i].aantal = nieuwAantal;
            localStorage.setItem("mandje", JSON.stringify(mandje));
            toonNavigatie();
            toonMandje();
        };

        rij.querySelector(".verwijder").onclick = () => {
            verwijderUitMandje(item);
        };

        container.appendChild(rij);
    }

    const totaalP = document.createElement("p");
    totaalP.style.marginTop = "12px";
    totaalP.innerHTML = `<strong>Totaal bij afhaling: € ${totaalPrijs.toFixed(2)}</strong>`;
    container.appendChild(totaalP);
}

function verwijderUitMandje(boek) {
    let mandje = JSON.parse(localStorage.getItem("mandje")) || [];
    mandje = mandje.filter(opgeslagen => opgeslagen.isbn !== boek.isbn);
    localStorage.setItem("mandje", JSON.stringify(mandje));

    toonNavigatie();
    toonMandje();
}

async function plaatsReservatie() {
    const melding = document.getElementById("mandje-melding");
    let mandje = JSON.parse(localStorage.getItem("mandje")) || [];

    if (mandje.length === 0) return;

    let isbns = [];
    for (let item of mandje) {
        for (let k = 0; k < item.aantal; k++) {
            isbns.push(item.isbn);
        }
    }

    try {
        const response = await fetch(URL + "/reservaties", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                gebruikerId: gebruiker.id,
                boekIsbns: isbns
            })
        });

        if (response.ok) {
            const resultaat = await response.json();
            melding.innerHTML = `<div class="melding-succes">${resultaat.bevestigingsBericht}</div>`;

            localStorage.removeItem("mandje");
            toonNavigatie();
            toonMandje();
            await haalMijnReviewsOp();
            await haalReservatiesOp();
        } else {
            melding.innerHTML = "<div class='melding-fout'>Reservatie mislukt: onvoldoende stock voor het gekozen aantal.</div>";
        }
    } catch (fout) {
        melding.innerHTML = "<div class='melding-fout'>Fout bij het verbinden met de backend.</div>";
    }
}

function heeftAlReviewVoorBoek(isbn) {
    for (let r of mijnReviews) {
        if (r.boekIsbn === isbn) {
            return true;
        }
    }
    return false;
}

async function haalMijnReviewsOp() {
    try {
        const response = await fetch(URL + "/reviews/gebruiker/" + gebruiker.id);
        mijnReviews = await response.json();
        mijnReviews.reverse();

        const container = document.getElementById("mijn-reviews-container");
        container.innerHTML = "";

        if (mijnReviews.length === 0) {
            container.innerHTML = "<p>Je hebt nog geen reviews geplaatst.</p>";
            return;
        }

        for (let r of mijnReviews) {
            const kaart = document.createElement("div");
            kaart.classList.add("card", "breed");
            kaart.innerHTML = `
                <p><strong>Boek:</strong> ${r.boekTitel}</p>
                <p class="klein"><strong>Geplaatst op:</strong> ${r.datum || "vandaag"}</p>
                <p><span class="sterren">${maakSterren(r.score)}</span></p>
                <p>${r.tekst}</p>
            `;
            container.appendChild(kaart);
        }
    } catch (fout) {
        console.error("Fout bij ophalen van mijn reviews:", fout);
    }
}

async function haalReservatiesOp() {
    try {
        const response = await fetch(URL + "/reservaties/gebruiker/" + gebruiker.id);
        const reservaties = await response.json();
        reservaties.reverse();

        const container = document.getElementById("reservaties-container");
        container.innerHTML = "";

        if (reservaties.length === 0) {
            container.innerHTML = "<p>Nog geen reservaties geplaatst.</p>";
            return;
        }

        for (let r of reservaties) {
            const kaart = document.createElement("div");
            kaart.classList.add("card", "breed");

            let uniekeBoeken = [];
            for (let i = 0; i < r.gereserveerdeIsbns.length; i++) {
                let isbn = r.gereserveerdeIsbns[i];
                let titel = r.gereserveerdeTitels[i];

                let bestaand = null;
                for (let u of uniekeBoeken) {
                    if (u.isbn === isbn) bestaand = u;
                }
                if (bestaand) {
                    bestaand.aantal++;
                } else {
                    uniekeBoeken.push({ isbn: isbn, titel: titel, aantal: 1 });
                }
            }

            kaart.innerHTML = `
                <p><strong>Reservatie #${r.reservatieId}</strong> — Besteld op: ${r.reservatieDatum}</p>
                <p class="groen">${r.bevestigingsBericht}</p>
                <div class="boeken-in-reservatie"></div>
            `;

            const boekenBlokContainer = kaart.querySelector(".boeken-in-reservatie");

            for (let b of uniekeBoeken) {
                const blok = document.createElement("div");
                blok.classList.add("boek-review-blok");

                if (heeftAlReviewVoorBoek(b.isbn)) {
                    blok.innerHTML = `
                        <p><strong>${b.aantal}x ${b.titel}</strong> — <span class="groen">Je hebt al een review geschreven voor dit boek.</span></p>
                    `;
                } else {
                    blok.innerHTML = `
                        <p><strong>${b.aantal}x ${b.titel}</strong></p>
                        <label>Score:</label>
                        <select class="rev-score">
                            <option value="5">★★★★★</option>
                            <option value="4">★★★★</option>
                            <option value="3">★★★</option>
                            <option value="2">★★</option>
                            <option value="1">★</option>
                        </select>
                        <input type="text" class="rev-tekst" placeholder="Schrijf jouw mening over ${b.titel}...">
                        <div class="rev-melding"></div>
                        <button class="rev-opslaan">Plaats review</button>
                    `;

                    blok.querySelector(".rev-opslaan").onclick = async () => {
                        const score = Number(blok.querySelector(".rev-score").value);
                        const tekst = blok.querySelector(".rev-tekst").value.trim();
                        const meldDiv = blok.querySelector(".rev-melding");

                        if (tekst.length < 1) {
                            meldDiv.innerHTML = "<div class='melding-fout'>Vul minstens 1 karakter in.</div>";
                            return;
                        }

                        const revRes = await fetch(URL + "/reviews", {
                            method: "POST",
                            headers: { "Content-Type": "application/json" },
                            body: JSON.stringify({
                                gebruikerId: gebruiker.id,
                                boekIsbn: b.isbn,
                                score: score,
                                tekst: tekst
                            })
                        });

                        if (revRes.ok) {
                            await haalMijnReviewsOp();
                            await haalReservatiesOp();
                        } else {
                            meldDiv.innerHTML = "<div class='melding-fout'>Je kan maar 1 review per boek plaatsen.</div>";
                        }
                    };
                }

                boekenBlokContainer.appendChild(blok);
            }

            container.appendChild(kaart);
        }
    } catch (fout) {
        console.error("Fout bij ophalen reservaties:", fout);
    }
}

window.onload = async () => {
    if (!gebruiker) {
        window.location.href = "login.html";
        return;
    }

    toonNavigatie();
    haalProfielOp();
    toonMandje();
    await haalMijnReviewsOp();
    await haalReservatiesOp();

    const reserveerKnop = document.getElementById("reserveer-alles");
    if (reserveerKnop) reserveerKnop.onclick = plaatsReservatie;
};