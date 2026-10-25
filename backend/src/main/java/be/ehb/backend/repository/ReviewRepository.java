package be.ehb.backend.repository;

import be.ehb.backend.model.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    List<Review> findByBoekIsbn(String isbn);
}