package be.ehb.backend.service;

import be.ehb.backend.dto.ReviewRequestDto;
import be.ehb.backend.dto.ReviewResponseDto;
import be.ehb.backend.model.Boek;
import be.ehb.backend.model.Gebruiker;
import be.ehb.backend.model.Review;
import be.ehb.backend.repository.BoekRepository;
import be.ehb.backend.repository.GebruikerRepository;
import be.ehb.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BoekRepository boekRepository;
    private final GebruikerRepository gebruikerRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         BoekRepository boekRepository,
                         GebruikerRepository gebruikerRepository) {
        this.reviewRepository = reviewRepository;
        this.boekRepository = boekRepository;
        this.gebruikerRepository = gebruikerRepository;
    }


    public ReviewResponseDto addReview(ReviewRequestDto dto) {
        if (!gebruikerRepository.existsById(dto.gebruikerId()) || !boekRepository.existsById(dto.boekIsbn())) {
            return null;
        }

        if (reviewRepository.existsByGebruikerIdAndBoekIsbn(dto.gebruikerId(), dto.boekIsbn())) {
            return null;
        }

        Gebruiker gebruiker = gebruikerRepository.findById(dto.gebruikerId()).get();
        Boek boek = boekRepository.findById(dto.boekIsbn()).get();

        boolean heeftGekocht = false;
        for (Boek gekocht : gebruiker.getGekochteBoeken()) {
            if (gekocht.getIsbn().equals(boek.getIsbn())) {
                heeftGekocht = true;
                break;
            }
        }

        if (!heeftGekocht) {
            return null;
        }

        Review review = new Review(boek, gebruiker, dto.score(), dto.tekst());
        Review opgeslagen = reviewRepository.save(review);

        return toDto(opgeslagen);
    }

    public List<ReviewResponseDto> getReviewsByBoekIsbn(String isbn) {
        List<ReviewResponseDto> lijst = new ArrayList<>();
        for (Review review : reviewRepository.findByBoekIsbn(isbn)) {
            lijst.add(toDto(review));
        }
        return lijst;
    }

    public ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getBoek().getIsbn(),
                review.getGebruiker().getGebruikersnaam(),
                review.getScore(),
                review.getTekst()
        );
    }
}