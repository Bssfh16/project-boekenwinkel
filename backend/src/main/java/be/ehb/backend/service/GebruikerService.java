package be.ehb.backend.service;

import be.ehb.backend.dto.BoekDto;
import be.ehb.backend.dto.LoginDto;
import be.ehb.backend.dto.ProfielDto;
import be.ehb.backend.dto.RegistratieDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.model.Gebruiker;
import be.ehb.backend.repository.GebruikerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service om te registreren, in te loggen en profielen te beheren.
 */
@Service
public class GebruikerService {

    private final GebruikerRepository gebruikerRepository;
    private final BoekService boekService;

    @Autowired
    public GebruikerService(GebruikerRepository gebruikerRepository, BoekService boekService) {
        this.gebruikerRepository = gebruikerRepository;
        this.boekService = boekService;
    }

    /**
     * Een nieuwe gebruiker registreren als e-mail en gebruikersnaam nog niet genomen zijn.
     */
    public ProfielDto registerGebruiker(RegistratieDto dto) {
        if (gebruikerRepository.existsByGebruikersnaam(dto.gebruikersnaam()) ||
                gebruikerRepository.existsByEmail(dto.email())) {
            return null;
        }
        Gebruiker nieuweGebruiker = new Gebruiker(dto.gebruikersnaam(), dto.email(), dto.wachtwoord());
        Gebruiker opgeslagen = gebruikerRepository.save(nieuweGebruiker);
        return toProfielDto(opgeslagen);
    }

    /**
     * nakijken of de gebruikersnaam en wachtwoord correct zijn.
     */
    public ProfielDto loginGebruiker(LoginDto dto) {
        Gebruiker gevonden = gebruikerRepository.findByGebruikersnaam(dto.gebruikersnaam());
        if (gevonden != null && gevonden.getWachtwoord().equals(dto.wachtwoord())) {
            return toProfielDto(gevonden);
        }
        return null;
    }

    /**
     * Profiel van een gebruiker via ID verkrijgen.
     */
    public ProfielDto getProfielById(int id) {
        if (gebruikerRepository.existsById(id)) {
            Gebruiker gevonden = gebruikerRepository.findById(id).get();
            return toProfielDto(gevonden);
        }
        return null;
    }

    /**
     * Gebruiker-entiteit omzetten naar ProfielDto.
     */
    public ProfielDto toProfielDto(Gebruiker gebruiker) {
        List<BoekDto> boekenLijst = new ArrayList<>();
        for (Boek boek : gebruiker.getGekochteBoeken()) {
            boekenLijst.add(boekService.toDto(boek));
        }
        return new ProfielDto(
                gebruiker.getId(),
                gebruiker.getGebruikersnaam(),
                gebruiker.getEmail(),
                boekenLijst
        );
    }
}