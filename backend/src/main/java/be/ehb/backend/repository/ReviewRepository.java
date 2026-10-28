package be.ehb.backend.repository;

import be.ehb.backend.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository om database-operaties toe te passen om review-entiteiten.
 */
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findByBoekIsbn(String isbn);
    List<Review> findByGebruikerId(int gebruikerId);
    boolean existsByGebruikerIdAndBoekIsbn(int gebruikerId, String isbn);
}