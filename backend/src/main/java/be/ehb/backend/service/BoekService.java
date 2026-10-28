package be.ehb.backend.service;

import be.ehb.backend.dto.BoekDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.repository.BoekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service om de boekencatalogus te beheren.
 */
@Service
public class BoekService {

    private final BoekRepository boekRepository;

    @Autowired
    public BoekService(BoekRepository boekRepository) {
        this.boekRepository = boekRepository;
    }

    /**
     * Alle boeken van de database verkrijgen.
     */
    public List<BoekDto> getAllBoeken() {
        List<BoekDto> boeken = new ArrayList<>();
        for (Boek boek : boekRepository.findAll()) {
            boeken.add(toDto(boek));
        }
        return boeken;
    }

    /**
     * 1 boek opzoeken via ISBN-nummer. Null als het boek niet bestaat.
     */
    public BoekDto getBoekByIsbn(String isbn) {
        if (boekRepository.existsById(isbn)) {
            Boek boek = boekRepository.findById(isbn).get();
            return toDto(boek);
        }
        return null;
    }

    /**
     * Boeken opzoeken als de titel overeenkomt met de zoekinput.
     */
    public List<BoekDto> searchBoekenOpTitel(String zoekterm) {
        List<BoekDto> resultaat = new ArrayList<>();
        for (Boek boek : boekRepository.findByTitelContainingIgnoreCase(zoekterm)) {
            resultaat.add(toDto(boek));
        }
        return resultaat;
    }

    /**
     * Een nieuw boek toevoegen of wijzigingen bewaren.
     */
    public BoekDto saveBoek(BoekDto dto) {
        Boek boek = new Boek(
                dto.isbn(),
                dto.titel(),
                dto.auteurs(),
                dto.descriptie(),
                dto.prijs(),
                dto.stock(),
                dto.genre()
        );
        Boek opgeslagen = boekRepository.save(boek);
        return toDto(opgeslagen);
    }

    /**
     * De stock van een boek aanpassen.
     */
    public BoekDto updateStock(String isbn, int newStock) {
        if (newStock < 0 || !boekRepository.existsById(isbn)) {
            return null;
        }
        Boek boek = boekRepository.findById(isbn).get();
        boek.setStock(newStock);
        return toDto(boekRepository.save(boek));
    }

    /**
     * Boekentiteit omzetten naar BoekDto.
     */
    public BoekDto toDto(Boek boek) {
        return new BoekDto(
                boek.getIsbn(),
                boek.getTitel(),
                boek.getAuteurs(),
                boek.getDescriptie(),
                boek.getPrijs(),
                boek.getStock(),
                boek.getGenre()
        );
    }
}