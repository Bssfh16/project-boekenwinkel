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

@RestController
@RequestMapping(value = "/reservaties")
@CrossOrigin(origins = "*")
public class ReservatieController {

    private final ReservatieService reservatieService;

    @Autowired
    public ReservatieController(ReservatieService reservatieService) {
        this.reservatieService = reservatieService;
    }

    @PostMapping
    public ResponseEntity<ReservatieResponseDto> addReservatie(@Valid @RequestBody ReservatieRequestDto dto) {
        ReservatieResponseDto bevestiging = reservatieService.addReservatie(dto);
        if (bevestiging != null) {
            return new ResponseEntity<>(bevestiging, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/gebruiker/{gebruikerId}")
    public ResponseEntity<List<ReservatieResponseDto>> getReservatiesByGebruiker(@PathVariable int gebruikerId) {
        return new ResponseEntity<>(reservatieService.getReservatiesByGebruiker(gebruikerId), HttpStatus.OK);
    }
}
