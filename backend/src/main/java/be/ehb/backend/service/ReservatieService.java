package be.ehb.backend.service;

import be.ehb.backend.dto.ReservatieRequestDto;
import be.ehb.backend.dto.ReservatieResponseDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.model.Gebruiker;
import be.ehb.backend.model.Reservatie;
import be.ehb.backend.repository.BoekRepository;
import be.ehb.backend.repository.GebruikerRepository;
import be.ehb.backend.repository.ReservatieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service om reservaties aan te maken en te verkrijgen. De stock en de afhaaldatum worden automatisch berekend.
 */
@Service
public class ReservatieService {

    private final ReservatieRepository reservatieRepository;
    private final BoekRepository boekRepository;
    private final GebruikerRepository gebruikerRepository;

    @Autowired
    public ReservatieService(ReservatieRepository reservatieRepository,
                             BoekRepository boekRepository,
                             GebruikerRepository gebruikerRepository) {
        this.reservatieRepository = reservatieRepository;
        this.boekRepository = boekRepository;
        this.gebruikerRepository = gebruikerRepository;
    }

    /**
     * Een nieuwe reservatie aanmaken als de stock van de boeken voldoende is. De stock verlaagt als een boek definitief gereserveerd en het boek wordt toegevoegd op de profiel.
     */
    public ReservatieResponseDto addReservatie(ReservatieRequestDto dto) {
        if (!gebruikerRepository.existsById(dto.gebruikerId())) {
            return null;
        }
        Gebruiker gebruiker = gebruikerRepository.findById(dto.gebruikerId()).get();

        List<Boek> teReserverenBoeken = new ArrayList<>();

        for (String isbn : dto.boekIsbns()) {
            if (!boekRepository.existsById(isbn)) {
                return null;
            }
            Boek boek = boekRepository.findById(isbn).get();

            int aantalKeerGevraagd = 0;
            for (String gevraagdIsbn : dto.boekIsbns()) {
                if (gevraagdIsbn.equals(isbn)) {
                    aantalKeerGevraagd++;
                }
            }

            if (boek.getStock() < aantalKeerGevraagd) {
                return null;
            }
            teReserverenBoeken.add(boek);
        }

        for (Boek boek : teReserverenBoeken) {
            boek.setStock(boek.getStock() - 1);
            boekRepository.save(boek);

            if (!heeftBoekAlGekocht(gebruiker, boek.getIsbn())) {
                gebruiker.getGekochteBoeken().add(boek);
            }
        }
        gebruikerRepository.save(gebruiker);

        LocalDate vandaag = LocalDate.now();
        LocalDate afhaalDatum = vandaag.plusDays(2);

        Reservatie reservatie = new Reservatie(gebruiker, teReserverenBoeken, vandaag, afhaalDatum, true);
        Reservatie opgeslagen = reservatieRepository.save(reservatie);

        return toDto(opgeslagen);
    }

    /**
     * Alle gemaakte reservaties van een bepaalde gebruiker verkrijgen.
     */
    public List<ReservatieResponseDto> getReservatiesByGebruiker(int gebruikerId) {
        List<ReservatieResponseDto> lijst = new ArrayList<>();
        for (Reservatie reservatie : reservatieRepository.findByGebruikerId(gebruikerId)) {
            lijst.add(toDto(reservatie));
        }
        return lijst;
    }

    /**
     * Methode om na te kijken of de gebruiker het boek al gereserveerd heeft of niet.
     */
    private boolean heeftBoekAlGekocht(Gebruiker gebruiker, String isbn) {
        for (Boek gekocht : gebruiker.getGekochteBoeken()) {
            if (gekocht.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reservatie-entiteit omzetten naar ReservatieResponseDto.
     */
    public ReservatieResponseDto toDto(Reservatie reservatie) {
        List<String> isbns = new ArrayList<>();
        List<String> titels = new ArrayList<>();
        for (Boek boek : reservatie.getGereserveerdeBoeken()) {
            isbns.add(boek.getIsbn());
            titels.add(boek.getTitel());
        }
        String bericht = "Bevestigd, je kan jouw gereserveerde boeken afhalen vanaf " + reservatie.getAfhaalDatum() + ".";

        return new ReservatieResponseDto(
                reservatie.getId(),
                reservatie.getGebruiker().getGebruikersnaam(),
                isbns,
                titels,
                reservatie.getReservatieDatum(),
                reservatie.getAfhaalDatum(),
                bericht
        );
    }
}