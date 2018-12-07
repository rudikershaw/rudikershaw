package main.dynamics.repositories;

import main.dynamics.entities.WhichpetDatum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/** Spring CRUD repository for WhichpetDatum entities. */
@Transactional
public interface WhichpetRepository extends CrudRepository<WhichpetDatum, Integer> {

    /**
     * Find all WhichpetDatum.
     * @param pageable the paging information for retrieval.
     * @return a Page of WhichpetDatum.
     */
    Page<WhichpetDatum> findAll(Pageable pageable);
}
