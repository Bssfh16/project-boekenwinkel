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
            if (boek.getStock() < 1) {
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

    public List<ReservatieResponseDto> getReservatiesByGebruiker(int gebruikerId) {
        List<ReservatieResponseDto> lijst = new ArrayList<>();
        for (Reservatie reservatie : reservatieRepository.findByGebruikerId(gebruikerId)) {
            lijst.add(toDto(reservatie));
        }
        return lijst;
    }

    private boolean heeftBoekAlGekocht(Gebruiker gebruiker, String isbn) {
        for (Boek gekocht : gebruiker.getGekochteBoeken()) {
            if (gekocht.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public ReservatieResponseDto toDto(Reservatie reservatie) {
        List<String> titels = new ArrayList<>();
        for (Boek boek : reservatie.getGereserveerdeBoeken()) {
            titels.add(boek.getTitel());
        }
        String bericht = "Bevestigd! Je kan je gereserveerde boeken afhalen en betalen vanaf " + reservatie.getAfhaalDatum() + ".";

        return new ReservatieResponseDto(
                reservatie.getId(),
                reservatie.getGebruiker().getGebruikersnaam(),
                titels,
                reservatie.getReservatieDatum(),
                reservatie.getAfhaalDatum(),
                bericht
        );
    }
}
