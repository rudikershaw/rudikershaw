package main.dynamics.repositories;

import main.dynamics.entities.WhichpetDatum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rudi Kershaw on 06/07/2016.
 */
@Transactional
public interface WhichpetRepository extends CrudRepository<WhichpetDatum, Integer> {
    Page<WhichpetDatum> findAll(Pageable pageable);
}
