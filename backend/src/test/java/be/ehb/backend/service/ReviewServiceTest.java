package be.ehb.backend.service;

import be.ehb.backend.dto.ReviewRequestDto;
import be.ehb.backend.dto.ReviewResponseDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.model.Gebruiker;
import be.ehb.backend.model.Review;
import be.ehb.backend.repository.BoekRepository;
import be.ehb.backend.repository.GebruikerRepository;
import be.ehb.backend.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    ReviewRepository reviewRepo;
    BoekRepository boekRepo;
    GebruikerRepository gebruikerRepo;
    ReviewService reviewService;

    Boek testBoek;
    Gebruiker koper;
    Gebruiker nietKoper;

    @BeforeEach
    public void init() {
        this.reviewRepo = Mockito.mock(ReviewRepository.class);
        this.boekRepo = Mockito.mock(BoekRepository.class);
        this.gebruikerRepo = Mockito.mock(GebruikerRepository.class);
        this.reviewService = new ReviewService(reviewRepo, boekRepo, gebruikerRepo);

        this.testBoek = new Boek("9789026167294", "De GVR", "Roald Dahl", "Reus", 29.99, 10, "Avontuur");

        this.koper = new Gebruiker("Loris", "Loris@student.ehb.be", "ehb@1234");
        this.koper.setId(1);
        this.koper.getGekochteBoeken().add(testBoek);

        this.nietKoper = new Gebruiker("Vanessa", "Vanessa@student.ehb.be", "ehb1234!");
        this.nietKoper.setId(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    public void isReviewAllowedForPurchasedBoek(int scoreInput) {
        when(gebruikerRepo.existsById(1)).thenReturn(true);
        when(gebruikerRepo.findById(1)).thenReturn(Optional.of(koper));
        when(boekRepo.existsById("9789026167294")).thenReturn(true);
        when(boekRepo.findById("9789026167294")).thenReturn(Optional.of(testBoek));

        Review opgeslagen = new Review(testBoek, koper, scoreInput, "Leuk boek!");
        when(reviewRepo.save(any(Review.class))).thenReturn(opgeslagen);

        ReviewRequestDto request = new ReviewRequestDto(1, "9789026167294", scoreInput, "Leuk boek!");
        ReviewResponseDto resultaat = reviewService.addReview(request);

        assertNotNull(resultaat);
        assertEquals(scoreInput, resultaat.score());
    }

    @Test
    public void isReviewBlockedWhenBoekNotPurchased() {
        when(gebruikerRepo.existsById(2)).thenReturn(true);
        when(gebruikerRepo.findById(2)).thenReturn(Optional.of(nietKoper));
        when(boekRepo.existsById("9789026167294")).thenReturn(true);
        when(boekRepo.findById("9789026167294")).thenReturn(Optional.of(testBoek));

        ReviewRequestDto request = new ReviewRequestDto(2, "9789026167294", 5, "Niet gekocht");
        ReviewResponseDto resultaat = reviewService.addReview(request);

        assertNull(resultaat);
        verify(reviewRepo, never()).save(any(Review.class));
    }
}