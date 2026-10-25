package be.ehb.backend.repository;

import be.ehb.backend.model.Reservatie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservatieRepository extends CrudRepository<Reservatie, Integer> {
    List<Reservatie> findByGebruikerId(int gebruikerId);
}
