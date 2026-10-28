package be.ehb.backend.repository;

import be.ehb.backend.model.Boek;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository om database-operaties toe te passen om boek-entiteiten.
 */
public interface BoekRepository extends CrudRepository<Boek, String> {
    List<Boek> findByTitelContainingIgnoreCase(String title);
}
