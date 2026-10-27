package be.ehb.backend.service;

import be.ehb.backend.dto.LoginDto;
import be.ehb.backend.dto.ProfielDto;
import be.ehb.backend.dto.RegistratieDto;
import be.ehb.backend.model.Gebruiker;
import be.ehb.backend.repository.GebruikerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GebruikerServiceTest {

    GebruikerRepository gebruikerRepo;
    BoekService boekService;
    GebruikerService gebruikerService;
    Gebruiker bestaandeGebruiker;

    @BeforeEach
    public void init() {
        this.gebruikerRepo = Mockito.mock(GebruikerRepository.class);
        this.boekService = Mockito.mock(BoekService.class);
        this.gebruikerService = new GebruikerService(gebruikerRepo, boekService);

        this.bestaandeGebruiker = new Gebruiker("Michiel", "Michiel@student.ehb.be", "Michiel1234@");
        this.bestaandeGebruiker.setId(1);
    }

    @Test
    public void isNewGebruikerRegisteredSuccessfully() {
        when(gebruikerRepo.existsByGebruikersnaam("Kristof")).thenReturn(false);
        when(gebruikerRepo.existsByEmail("Kristof@student.ehb.be")).thenReturn(false);

        Gebruiker opgeslagen = new Gebruiker("Kristof", "Kristof@student.ehb.be", "Kristof9871.");
        opgeslagen.setId(2);
        when(gebruikerRepo.save(any(Gebruiker.class))).thenReturn(opgeslagen);

        RegistratieDto dto = new RegistratieDto("Kristof", "Kristof@student.ehb.be", "Kristof9871.");
        ProfielDto resultaat = gebruikerService.registerGebruiker(dto);

        assertNotNull(resultaat);
        assertEquals("Kristof", resultaat.gebruikersnaam());
    }

    @Test
    public void isDuplicateGebruikersnaamRejected() {
        when(gebruikerRepo.existsByGebruikersnaam("Michiel")).thenReturn(true);

        RegistratieDto dto = new RegistratieDto("Michiel", "Michiels@student.ehb.be", "Michiel12345");
        ProfielDto resultaat = gebruikerService.registerGebruiker(dto);

        assertNull(resultaat);
        verify(gebruikerRepo, never()).save(any(Gebruiker.class));
    }

    @Test
    public void isDuplicateEmailRejected() {
        when(gebruikerRepo.existsByGebruikersnaam("Peter")).thenReturn(false);
        when(gebruikerRepo.existsByEmail("Michiel@student.ehb.be")).thenReturn(true);

        RegistratieDto dto = new RegistratieDto("Peter", "Michiel@student.ehb.be", "Michiel12345");
        ProfielDto resultaat = gebruikerService.registerGebruiker(dto);

        assertNull(resultaat);
    }

    @Test
    public void isLoginWithWrongPasswordRejected() {
        when(gebruikerRepo.findByGebruikersnaam("Michiel")).thenReturn(bestaandeGebruiker);

        LoginDto fouteLogin = new LoginDto("Michiel", "Michiel3456");
        ProfielDto resultaat = gebruikerService.loginGebruiker(fouteLogin);

        assertNull(resultaat);
    }

    @Test
    public void isLoginWithCorrectPasswordAccepted() {
        when(gebruikerRepo.findByGebruikersnaam("Michiel")).thenReturn(bestaandeGebruiker);

        LoginDto goedeLogin = new LoginDto("Michiel", "Michiel1234@");
        ProfielDto resultaat = gebruikerService.loginGebruiker(goedeLogin);

        assertNotNull(resultaat);
        assertEquals("Michiel", resultaat.gebruikersnaam());
    }
}