package be.ehb.backend.service;

import be.ehb.backend.dto.ReservatieRequestDto;
import be.ehb.backend.dto.ReservatieResponseDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.model.Gebruiker;
import be.ehb.backend.model.Reservatie;
import be.ehb.backend.repository.BoekRepository;
import be.ehb.backend.repository.GebruikerRepository;
import be.ehb.backend.repository.ReservatieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests om de stock te verlagen, de afhaaldatum te bepalen en de reservatie te weigeren bij onvoldoende stock.
 */
class ReservatieServiceTest {

    ReservatieRepository reservatieRepo;
    BoekRepository boekRepo;
    GebruikerRepository gebruikerRepo;
    ReservatieService reservatieService;

    Gebruiker testGebruiker;
    Boek beschikbaarBoek;
    Boek uitverkochtBoek;

    /**
     * Voor elke test een mock-repository, een beschikbaar boek en een testgebruiker en een uitverkocht boek zetten.
     */
    @BeforeEach
    public void init() {
        this.reservatieRepo = Mockito.mock(ReservatieRepository.class);
        this.boekRepo = Mockito.mock(BoekRepository.class);
        this.gebruikerRepo = Mockito.mock(GebruikerRepository.class);
        this.reservatieService = new ReservatieService(reservatieRepo, boekRepo, gebruikerRepo);

        this.testGebruiker = new Gebruiker("Sandoval", "Sandoval@student.ehb.be", "Sansan987!");
        this.testGebruiker.setId(1);

        this.beschikbaarBoek = new Boek("9789026167294", "De GVR", "Roald Dahl", "Grote Vriendelijke Reus.", 29.99, 3, "Avontuur");
        this.uitverkochtBoek = new Boek("9789026169786", "Matilda", "Roald Dahl", "Meisje dat niet normaal is.", 24.99, 0, "Fantasie");
    }

    /**
     * Nagaan of de stock met 1 daalt en de afhaaldtatum bepaald wordt met +2 dagen bij een correcte reservatie. Het boek moet ook aan de gereserveerde boeken van de gebruiker toegevoegd worden.
     */
    @Test
    public void isStockDecreasedAndAfhaalDatumSet() {
        when(gebruikerRepo.existsById(1)).thenReturn(true);
        when(gebruikerRepo.findById(1)).thenReturn(Optional.of(testGebruiker));
        when(boekRepo.existsById("9789026167294")).thenReturn(true);
        when(boekRepo.findById("9789026167294")).thenReturn(Optional.of(beschikbaarBoek));

        Reservatie opgeslagen = new Reservatie(
                testGebruiker,
                List.of(beschikbaarBoek),
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                true
        );
        when(reservatieRepo.save(any(Reservatie.class))).thenReturn(opgeslagen);

        ReservatieRequestDto request = new ReservatieRequestDto(1, List.of("9789026167294"));
        ReservatieResponseDto resultaat = reservatieService.addReservatie(request);

        assertNotNull(resultaat);
        assertEquals(2, beschikbaarBoek.getStock());
        assertEquals(LocalDate.now().plusDays(2), resultaat.afhaalDatum());
        assertEquals(1, testGebruiker.getGekochteBoeken().size());
    }

    /**
     * Checken of de reservatie geweigerd wordt als de stock onvoldoende is.
     */
    @Test
    public void isReservatieRefusedWhenOutOfStock() {
        when(gebruikerRepo.existsById(1)).thenReturn(true);
        when(gebruikerRepo.findById(1)).thenReturn(Optional.of(testGebruiker));
        when(boekRepo.existsById("9789026169786")).thenReturn(true);
        when(boekRepo.findById("9789026169786")).thenReturn(Optional.of(uitverkochtBoek));

        ReservatieRequestDto request = new ReservatieRequestDto(1, List.of("9789026169786"));
        ReservatieResponseDto resultaat = reservatieService.addReservatie(request);

        assertNull(resultaat);
        assertEquals(0, uitverkochtBoek.getStock());
        verify(reservatieRepo, never()).save(any(Reservatie.class));
    }

    /**
     * Checken of de volledige reservatie gestopt wordt en voorraad ongewijzigd blijft als de stock van het tweede boek onvoldoende is.
     */
    @Test
    public void isFirstBoekStockUnchangedWhenSecondBoekIsOutOfStock() {
        when(gebruikerRepo.existsById(1)).thenReturn(true);
        when(gebruikerRepo.findById(1)).thenReturn(Optional.of(testGebruiker));
        when(boekRepo.existsById("9789026167294")).thenReturn(true);
        when(boekRepo.findById("9789026167294")).thenReturn(Optional.of(beschikbaarBoek));
        when(boekRepo.existsById("9789026169786")).thenReturn(true);
        when(boekRepo.findById("9789026169786")).thenReturn(Optional.of(uitverkochtBoek));

        ReservatieRequestDto request = new ReservatieRequestDto(1, List.of("9789026167294", "9789026169786"));
        ReservatieResponseDto resultaat = reservatieService.addReservatie(request);

        assertNull(resultaat);
        assertEquals(3, beschikbaarBoek.getStock());
        assertEquals(0, testGebruiker.getGekochteBoeken().size());
    }
}