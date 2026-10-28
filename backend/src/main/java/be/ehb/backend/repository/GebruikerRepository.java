package be.ehb.backend.repository;

import be.ehb.backend.model.Gebruiker;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository om database-operaties toe te passen om gebruiker-entiteiten.
 */
public interface GebruikerRepository extends CrudRepository<Gebruiker, Integer> {

    Gebruiker findByGebruikersnaam(String gebruikersnaam);
    boolean existsByGebruikersnaam(String gebruikersnaam);
    boolean existsByEmail(String email);
}