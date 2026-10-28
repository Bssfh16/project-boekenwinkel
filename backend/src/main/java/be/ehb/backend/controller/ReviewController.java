package be.ehb.backend.controller;

import be.ehb.backend.dto.ReviewRequestDto;
import be.ehb.backend.dto.ReviewResponseDto;
import be.ehb.backend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller voor het maken en verkrijgen van reviews.
 */
@RestController
@RequestMapping(value = "/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Alle reviews voor een bepaald boek verkrijgen.
     * GET /reviews/boek/{isbn}
     */
    @GetMapping("/boek/{isbn}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBoek(@PathVariable String isbn) {
        return new ResponseEntity<>(reviewService.getReviewsByBoekIsbn(isbn), HttpStatus.OK);
    }

    /**
     * Alle reviews voor een bepaalde gebruiker verkrijgen.
     * GET /reviews/gebruiker/{gebruikerId}
     */
    @GetMapping("/gebruiker/{gebruikerId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByGebruiker(@PathVariable int gebruikerId) {
        return new ResponseEntity<>(reviewService.getReviewsByGebruikerId(gebruikerId), HttpStatus.OK);
    }

    /**
     * Nieuwe review aanmaken voor een gekocht boek.
     * POST /reviews
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(@Valid @RequestBody ReviewRequestDto dto) {
        ReviewResponseDto aangemaakt = reviewService.addReview(dto);
        if (aangemaakt != null) {
            return new ResponseEntity<>(aangemaakt, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
