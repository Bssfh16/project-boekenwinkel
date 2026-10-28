package be.ehb.backend.controller;

import be.ehb.backend.dto.ReservatieRequestDto;
import be.ehb.backend.dto.ReservatieResponseDto;
import be.ehb.backend.service.ReservatieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller voor het maken en bekijken van boekreservaties.
 */
@RestController
@RequestMapping(value = "/reservaties")
@CrossOrigin(origins = "*")
public class ReservatieController {

    private final ReservatieService reservatieService;

    @Autowired
    public ReservatieController(ReservatieService reservatieService) {
        this.reservatieService = reservatieService;
    }

    /**
     * Nieuwe reservaties voor een gebruiker aanmaken.
     */
    @PostMapping
    public ResponseEntity<ReservatieResponseDto> addReservatie(@Valid @RequestBody ReservatieRequestDto dto) {
        ReservatieResponseDto bevestiging = reservatieService.addReservatie(dto);
        if (bevestiging != null) {
            return new ResponseEntity<>(bevestiging, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Alle reservaties van een gebruiker verkrijgen.
     */
    @GetMapping("/gebruiker/{gebruikerId}")
    public ResponseEntity<List<ReservatieResponseDto>> getReservatiesByGebruiker(@PathVariable int gebruikerId) {
        return new ResponseEntity<>(reservatieService.getReservatiesByGebruiker(gebruikerId), HttpStatus.OK);
    }
}
