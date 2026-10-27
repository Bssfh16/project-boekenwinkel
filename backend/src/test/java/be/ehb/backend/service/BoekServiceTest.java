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

class BoekServiceTest {

    BoekRepository boekRepo;
    BoekService boekService;
    Boek testBoek;

    @BeforeEach
    public void init() {
        this.boekRepo = Mockito.mock(BoekRepository.class);
        this.boekService = new BoekService(boekRepo);
        this.testBoek = new Boek("9789026167294", "De GVR", "Roald Dahl", "Grote Vriendelijke Reus", 29.99, 10, "Avontuur");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -100})
    public void isNegativeStockRejected(int fouteStock) {
        BoekDto resultaat = boekService.updateStock("9789026167294", fouteStock);
        assertNull(resultaat);
    }

    @Test
    public void isUpdateStockNullWhenBoekDoesNotExist() {
        when(boekRepo.existsById("0000000000000")).thenReturn(false);

        BoekDto resultaat = boekService.updateStock("0000000000000", 10);

        assertNull(resultaat);
    }

    @Test
    public void isSearchBoekenOpTitelWorking() {
        when(boekRepo.findByTitelContainingIgnoreCase("GVR")).thenReturn(List.of(testBoek));

        List<BoekDto> resultaat = boekService.searchBoekenOpTitel("GVR");

        assertEquals(1, resultaat.size());
        assertEquals("De GVR", resultaat.get(0).titel());
    }

    @Test
    public void isGetBoekByIsbnWorkingWhenExists() {
        when(boekRepo.existsById("9789026167294")).thenReturn(true);
        when(boekRepo.findById("9789026167294")).thenReturn(Optional.of(testBoek));

        BoekDto resultaat = boekService.getBoekByIsbn("9789026167294");

        assertNotNull(resultaat);
        assertEquals("Roald Dahl", resultaat.auteurs());
    }
}