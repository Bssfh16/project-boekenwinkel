package be.ehb.backend.model;

import be.ehb.backend.repository.BoekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * De database aanvullen met specifieke boeken bij het opstarten van de applicatie.
 */
@Component
public class BoekDataSource implements CommandLineRunner {

    private final BoekRepository boekRepository;

    @Autowired
    public BoekDataSource(BoekRepository boekRepository) {
        this.boekRepository = boekRepository;
    }

    @Override
    public void run(String... args) {
        if (boekRepository.count() == 0) {
            boekRepository.save(new Boek(
                    "9789026167294",
                    "De GVR",
                    "Roald Dahl",
                    "Sofie wordt door een reus ontvoerd! Maar hoewel deze Grote Vriendelijke Reus de vreemdste woorden gebruikt, is hij wel heel aardig. Wat hij Sofie echter vertelt over de andere reuzen is vreselijk.",
                    29.99,
                    10,
                    "Avontuur"
            ));

            boekRepository.save(new Boek(
                    "9789026169786",
                    "Matilda",
                    "Roald Dahl",
                    "Matilda is briljant. Ze is een gevoelig meisje dat geweldig goed kan leren. Toen ze anderhalf was, kon Matilda al praten als een volwassene, en al vóór ze vijf was, las ze boeken van wereldberoemde schrijvers. Maar haar ouders vinden haar maar lastig en behandelen haar als ongedierte.\n" +
                            "Matilda besluit dat het tijd is voor actie. Ze bedenkt slimme straffen voor haar ouders. En als het gruwelijke schoolhoofd juffrouw Bulstronk haar ook wil aanpakken, ontdekt Matilda dat ze iets kan wat niemand anders kan.",
                    24.99,
                    0,
                    "Fantasie"
            ));

            boekRepository.save(new Boek(
                    "9789076174105",
                    "Harry Potter en de steen der wijzen",
                    "Joanne Kathleen Rowling",
                    "Harry Potter is een doodgewone, maar ongelukkige jongen die sinds de dood van zijn ouders bij zijn saaie en hardvochtige oom en tante woont, in de bezemkast onder de trap.\n" +
                            "Op een dag arriveert er een geheimzinnige brief voor hem.",
                    34.99,
                    18,
                    "Fantasie"
            ));

            boekRepository.save(new Boek(
                    "9789047632931",
                    "Disney's Gouden Verhalen voor het slapengaan",
                    "Disney",
                    "De tanden zijn gepoetst, het kussen is opgeschud, het nachtlampje brandt... Tijd voor een Disneyverhaal voor het slapengaan!",
                    27.49,
                    13,
                    "Animatie"
            ));

            boekRepository.save(new Boek(
                    "9789000390625",
                    "The Hunger Games - The Ballad of Songbirds and Snakes",
                    "Suzanne Collins",
                    "In het Capitool bereidt de achttienjarige Coriolanus Snow zich voor op zijn enige kans op roem en succes, als mentor in de tiende Hongerspelen. Zijn ooit zo machtige familie heeft het moeilijk en hoe het hun verder zal vergaan, hangt af van Coriolanus’ prestaties.",
                    16.89,
                    22,
                    "Avontuur"
            ));
        }
    }
}
