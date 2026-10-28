package be.ehb.backend.controller;

import be.ehb.backend.dto.LoginDto;
import be.ehb.backend.dto.ProfielDto;
import be.ehb.backend.dto.RegistratieDto;
import be.ehb.backend.service.GebruikerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller voor het registreren, inloggen en verkrijgen van een gebruikersprofiel.
 */
@RestController
@RequestMapping(value = "/gebruikers")
@CrossOrigin(origins = "*")
public class GebruikerController {

    private final GebruikerService gebruikerService;

    @Autowired
    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    /**
     * Een nieuwe gebruiker registreren.
     * POST /gebruikers/register
     */
    @PostMapping("/register")
    public ResponseEntity<ProfielDto> registerGebruiker(@Valid @RequestBody RegistratieDto dto) {
        ProfielDto aangemaakt = gebruikerService.registerGebruiker(dto);
        if (aangemaakt != null) {
            return new ResponseEntity<>(aangemaakt, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Een bestaande gebruiker inloggen en profiel weergeven.
     * POST /gebruikers/login
     */
    @PostMapping("/login")
    public ResponseEntity<ProfielDto> loginGebruiker(@Valid @RequestBody LoginDto dto) {
        ProfielDto ingelogd = gebruikerService.loginGebruiker(dto);
        if (ingelogd != null) {
            return new ResponseEntity<>(ingelogd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * De gekochte boeken en profiel van een gebruiker verkrijgen.
     * GET /gebruikers/{id}/profiel
     */
    @GetMapping("/{id}/profiel")
    public ResponseEntity<ProfielDto> getProfielById(@PathVariable int id) {
        ProfielDto profiel = gebruikerService.getProfielById(id);
        if (profiel != null) {
            return new ResponseEntity<>(profiel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
