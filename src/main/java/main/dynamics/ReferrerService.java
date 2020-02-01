package main.dynamics;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dynamics.entities.Article;
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

    /**
     * Process a request and article and add a new referrer for the provided article.
     * If this referrer is already registered against the article, increment the count.
     *
     * @param request the request who's referrer is to be checked.
     * @param article the article to add a referrer for.
     * @return true if a Referrer entity is created or updated.
     */
    public boolean processReferrer(final HttpServletRequest request, final Article article) {
        return false;
    }
}
