package main.dynamics.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.dynamics.entities.Referer;
import main.dynamics.entities.RefererIdentity;

/** Spring CRUD repository for referers. */
@Repository
@Transactional
public interface RefererRepository extends CrudRepository<Referer, RefererIdentity> {

    /**
     * List all referers for the given article.
     *
     * @param id the ID of an article to find referers for.
     * @return a list of referers which referred to this article.
     */
    List<Referer> findAllByRefererIdentityArticleId(Integer id);

    /**
     * Find an entity by referer for the given article.
     *
     * @param referer the referer URL to find.
     * @param id the ID of an article to find a referer for.
     * @return a referer with a given URL referring to this article.
     */
    Referer findByRefererIdentityRefererAndRefererIdentityArticleId(String referer, Integer id);
}
