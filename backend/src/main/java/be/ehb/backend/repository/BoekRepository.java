package be.ehb.backend.repository;

import be.ehb.backend.model.Boek;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BoekRepository extends CrudRepository<Boek, String> {
    List<Boek> findByTitleContainingIgnoreCase(String title);
}
