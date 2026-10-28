package be.ehb.backend.service;

import be.ehb.backend.dto.BoekDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.repository.BoekRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests om boeken te verkrijgen, op te zoeken en de stock aan te passen.
 */
class BoekServiceTest {

    BoekRepository boekRepo;
    BoekService boekService;
    Boek testBoek;

    /**
     * Voor elke test een nieuwe mock BoekRepository en testboek aanmaken
     */
    @BeforeEach
    public void init() {
        this.boekRepo = Mockito.mock(BoekRepository.class);
        this.boekService = new BoekService(boekRepo);
        this.testBoek = new Boek("9789026167294", "De GVR", "Roald Dahl", "Grote Vriendelijke Reus", 29.99, 10, "Avontuur");
    }

    /**
     * Nakijken of een negatieve stock geweigerd wordt.
     * @param fouteStock
     */
    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    public void isNegativeStockRejected(int fouteStock) {
        BoekDto resultaat = boekService.updateStock("9789026167294", fouteStock);
        assertNull(resultaat);
    }

    /**
     * Nakijken of er niets teruggegeven wordt wanneer ISBN-nummer niet bestaat.
     */
    @Test
    public void isUpdateStockNullWhenBoekDoesNotExist() {
        when(boekRepo.existsById("0000000000000")).thenReturn(false);

        BoekDto resultaat = boekService.updateStock("0000000000000", 10);

        assertNull(resultaat);
    }

    /**
     * De correcte lijst BoekDto testen met het opzoeken van de titel van het boek.
     */
    @Test
    public void isSearchBoekenOpTitelWorking() {
        when(boekRepo.findByTitelContainingIgnoreCase("GVR")).thenReturn(List.of(testBoek));

        List<BoekDto> resultaat = boekService.searchBoekenOpTitel("GVR");

        assertEquals(1, resultaat.size());
        assertEquals("De GVR", resultaat.get(0).titel());
    }

    /**
     * Nakijken of correcte titel boek teruggegeven wordt als ISBN-nummer in de database bestaat.
     */
    @Test
    public void isGetBoekByIsbnWorkingWhenExists() {
        when(boekRepo.existsById("9789026167294")).thenReturn(true);
        when(boekRepo.findById("9789026167294")).thenReturn(Optional.of(testBoek));

        BoekDto resultaat = boekService.getBoekByIsbn("9789026167294");

        assertNotNull(resultaat);
        assertEquals("Roald Dahl", resultaat.auteurs());
    }
}