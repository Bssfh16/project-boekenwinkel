package be.ehb.backend.controller;

import be.ehb.backend.dto.BoekDto;
import be.ehb.backend.service.BoekService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests om de juiste HTTP-statuscodes te checken.
 */
class BoekControllerTest {

    BoekService boekService;
    BoekController boekController;

    /**
     * Voor elke test een mock BoekService maken en het sturen naar BoekController.
     */
    @BeforeEach
    public void init() {
        this.boekService = Mockito.mock(BoekService.class);
        this.boekController = new BoekController(boekService);
    }

    @Test
    public void expectedResultOk() {
        BoekDto testBoek = new BoekDto("9789026167294", "De GVR", "Roald Dahl", "Reus", 29.99, 10, "Avontuur");
        when(boekService.getBoekByIsbn("9789026167294")).thenReturn(testBoek);

        ResponseEntity<BoekDto> response = boekController.getBoekByIsbn("9789026167294");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Testen of .200, OK teruggeeft als het boeg gevonden wordt.
     */
    @Test
    public void expectedResultNotFound() {
        when(boekService.getBoekByIsbn("0000000000000")).thenReturn(null);

        ResponseEntity<BoekDto> response = boekController.getBoekByIsbn("0000000000000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Testen of .404, NOT_FOUND teruggegeven wordt als het boek niet bestaat.
     */
    @Test
    public void expectedResultBadRequest() {
        when(boekService.updateStock("9789026167294", -5)).thenReturn(null);

        ResponseEntity<BoekDto> response = boekController.updateStock("9789026167294", -5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}