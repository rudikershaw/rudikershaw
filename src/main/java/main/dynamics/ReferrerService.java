package main.dynamics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dynamics.entities.Referrer;
import main.dynamics.repositories.ReferrerRepository;

/** Referrer service. */
@Service
public class ReferrerService {

    /** Referrer repo. */
    private final ReferrerRepository repository;

    /**
     * Constructor used for autowiring.
     * @param repository autowired article repo.
     */
    @Autowired
    public ReferrerService(final ReferrerRepository repository) {
       this.repository = repository;
    }

    /**
     * List all referrers for a particular article by it's ID.
     *
     * @param id the ID of the article to find referrers for.
     * @return a list of referrer entities.
     */
    public List<Referrer> listByArticleId(final int id) {
        return repository.findAllByReferrerIdentityArticleId(id);
    }
}
