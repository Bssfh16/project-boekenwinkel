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

/**
 * Service om reviews toe te voegen en te verkrijgen. Controle of een boek gereserveerd is en er bestaat een limiet van 1 review per gebruiker per boek.
 */
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

    /**
     * Een nieuwe review toevoegen als de gebruiker het boek gereserveerd heeft en zijn mening nog niet heeft gegeven.
     */
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

    /**
     * Een lijst van alle reviews voor een bepaald boek, op basis van de ISBN, weergeven.
     */
    public List<ReviewResponseDto> getReviewsByBoekIsbn(String isbn) {
        List<ReviewResponseDto> lijst = new ArrayList<>();
        for (Review review : reviewRepository.findByBoekIsbn(isbn)) {
            lijst.add(toDto(review));
        }
        return lijst;
    }

    /**
     * Een lijst van alle reviews van een bepaalde gebruiker weergeven.
     */
    public List<ReviewResponseDto> getReviewsByGebruikerId(int gebruikerId) {
        List<ReviewResponseDto> lijst = new ArrayList<>();
        for (Review review : reviewRepository.findByGebruikerId(gebruikerId)) {
            lijst.add(toDto(review));
        }
        return lijst;
    }

    /**
     * Review-eniteit omzetten naar ReviewResponseDto.
     */
    public ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getBoek().getIsbn(),
                review.getBoek().getTitel(),
                review.getGebruiker().getGebruikersnaam(),
                review.getScore(),
                review.getTekst(),
                review.getDatum()
        );
    }
}