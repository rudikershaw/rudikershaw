package main.dynamics;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dynamics.entities.Article;
import main.dynamics.entities.Referer;
import main.dynamics.entities.RefererIdentity;
import main.dynamics.repositories.RefererRepository;

/** Referer service. */
@Service
public class RefererService {

    /** Referer repo. */
    private final RefererRepository repository;

    /**
     * Constructor used for autowiring.
     * @param repository autowired article repo.
     */
    @Autowired
    public RefererService(final RefererRepository repository) {
       this.repository = repository;
    }

    /**
     * List all referers for a particular article by it's ID.
     *
     * @param id the ID of the article to find referers for.
     * @return a list of referer entities.
     */
    public List<Referer> listByArticleId(final int id) {
        return repository.findAllByRefererIdentityArticleId(id);
    }

    /**
     * Process a request and article and add a new referer for the provided article.
     * If this referer is already registered against the article, increment the count.
     *
     * @param request the request who's referer is to be checked.
     * @param article the article to add a referer for.
     */
    public void processReferer(final HttpServletRequest request, final Article article) {
        final String referer = request.getHeader("Referer");
        if (!Strings.isBlank(referer)) {
            final Referer entity = repository.findByRefererIdentityRefererAndRefererIdentityArticleId(referer, article.getId());
            if (entity == null) {
                repository.save(new Referer(new RefererIdentity(article, referer)));
            } else {
                entity.setCount(entity.getCount() + 1);
                repository.save(entity);
            }
        }
    }
}
