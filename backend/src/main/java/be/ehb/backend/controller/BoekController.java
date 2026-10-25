package be.ehb.backend.controller;

import be.ehb.backend.dto.BoekDto;
import be.ehb.backend.service.BoekService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boeken")
@CrossOrigin(origins = "*")
public class BoekController {

    private final BoekService boekService;

    @Autowired
    public BoekController(BoekService boekService) {
        this.boekService = boekService;
    }

    @GetMapping
    public ResponseEntity<List<BoekDto>> getAllBoeken() {
        return new ResponseEntity<>(boekService.getAllBoeken(), HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BoekDto> getBoekByIsbn(@PathVariable String isbn) {
        BoekDto gevonden = boekService.getBoekByIsbn(isbn);
        if (gevonden != null) {
            return new ResponseEntity<>(gevonden, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<BoekDto>> searchBoeken(@RequestParam String searchQuery) {
        return new ResponseEntity<>(boekService.searchBoekenOpTitel(searchQuery), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BoekDto> addBoek(@Valid @RequestBody BoekDto dto) {
        BoekDto created = boekService.saveBoek(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{isbn}/stock")
    public ResponseEntity<BoekDto> updateStock(@PathVariable String isbn, @RequestParam int stock) {
        BoekDto aangepast = boekService.updateStock(isbn, stock);
        if (aangepast != null) {
            return new ResponseEntity<>(aangepast, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}