# Project Boekenwinkel
Dit project hoort bij het vak "Application Architecture".
Het onderwerp van de website is het reserveren van boeken van een boekenwinkel met gebruikers en reviews.

# Planning
- Zaterdag 24/10
  - Project aanmaken met dependencies.
  - H2-database configureren en instellen.
  - Spring Boot project aanmaken.
  - 4 modellen aanmaken.
- Zondag 25/10
  - 4 repositories aanmaken.
  - Alle bijbehorende dto's aanmaken.
  - 4 services aanmaken.
  - 4 controllers aanmaken.
  - BoekDataSource aanmaken met echte data boeken.
- Maandag 26/10
  - Alle services-functionaliteiten die kunnen foutlopen, testen.
  - 1 controller testen.
  - Debuggen wanneer nodig.
- Dinsdag 27/10
  - Frontend:
    - 4 html-pagina's aanmaken.
    - boekenwinkel.css toepassen op html.
    - 5 .js aanmaken voor logica.
  - Debuggen wanneer nodig.
- Woensdag 28/10
  - Alles nog een laatste keer checken.
  - .README aanvullen
  - Comments bij code toevoegen.

# Gebruikte Technologieën
- Java
- Spring Boot
    - Dependencies:
        - Spring Web
        - H2 Database
        - Spring Data Jpa
        - Validation
        - Spring Boot Actuator
  - Maven
  - HTML
  - CSS
  - Javascript

# Functionaliteiten
- Het weergeven van alle boeken van de database op de cataloguspagina.
- De mogelijkheid hebben om zich te registreren met een gebruikersnaam, email en wachtwoord.
- De mogelijkheid hebben om zich in te loggen met gebruikersnaam en wachtwoord.
- Het reserveren van boeken voor ingelogde gebruikers als de stock voldoende is.
- Het weergeven van: de gegevens van de gebruiker, alle reservaties van een gebruiker, alle boeken in het winkelmandje en alle reviews van een gebruiker op de profielpagina.
- Het posten van reviews voor een bepaald boek met behulp van sterren (score 1-5) en tekst.
- De mogelijkheid hebben om zich uit te loggen.
- Het bekijken van de catalogus voor alle websitebezoekers.

# Installatiehandleiding
- Download IDEA Intellij, in het geval dit nog niet gedaan is.
- Clone de Github Repository.
- Start de backend-applicatie.
  - Open project-boekenwinkel/backend/src/main/java/be.ehb/backend.
  - Start BackendApplication.
- Open nu de pagina index.html om de website visueel te verkrijgen
  - Rechtermuisknop op frontend/index.html
  - Klikken op "Open in".
  - Klikken op "Browser".
  - Kies de browser die je wilt gebruiken.
- Nu kan je door alle html-pagina's bladeren.
- Maak een gebruiker aan om alle functionaliteiten van de website te kunnen gebruiken.

# Endpoints
- Backend = localhost:8080
  - /h2-console
    - om alle tabellen, en hun inhoud, van de database te bekijken.
    - Gebruik volgende inloggegevens om de database te bekijken, deze zijn ook verkrijgbaar in project-boekenwinkel/backend/src/main/resources/application.properties
      - Driver Class = org.h2.Driver
      - JDBC URL = jdbc:h2:file:./data/boekenwinkel 
      - User Name = sa
      - Password = (leeg, niets schrijven)
- Frontend = localhost:63342/project-boekenwinkel/frontend
  - /index.html
  - /boek.html
  - /login.html
  - /profiel.html


# Bronnen
- Cursus "Application Architecture"; raadpleegbaar via canvas.ehb.be
- h2-database documentatie
  - https://h2database.com/html/quickstart.html
- AI-tool: Google Gemini
  - https://share.gemini.google/jrragdadF76L
  - https://share.gemini.google/HntVYHgSTSLt
  - https://share.gemini.google/Fi659YxWzZD1
  - https://share.gemini.google/BwlidddhHiLO
  - https://share.gemini.google/OuXbrrlWSWM5
  - https://share.gemini.google/B44ichIk3mht
  - https://share.gemini.google/2gNFhHO6wfcI

# Sterk Aangeraden Materiaal
- IDEA Intellij
- Internet Browser
