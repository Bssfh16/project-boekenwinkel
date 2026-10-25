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

@RestController
@RequestMapping(value = "/gebruikers")
@CrossOrigin(origins = "*")
public class GebruikerController {

    private final GebruikerService gebruikerService;

    @Autowired
    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    @PostMapping("/register")
    public ResponseEntity<ProfielDto> registerGebruiker(@Valid @RequestBody RegistratieDto dto) {
        ProfielDto aangemaakt = gebruikerService.registerGebruiker(dto);
        if (aangemaakt != null) {
            return new ResponseEntity<>(aangemaakt, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ProfielDto> loginGebruiker(@Valid @RequestBody LoginDto dto) {
        ProfielDto ingelogd = gebruikerService.loginGebruiker(dto);
        if (ingelogd != null) {
            return new ResponseEntity<>(ingelogd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

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
