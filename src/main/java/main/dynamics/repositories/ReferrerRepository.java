package main.dynamics.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.dynamics.entities.Referrer;
import main.dynamics.entities.ReferrerIdentity;

/** Spring CRUD repository for referrers. */
@Repository
@Transactional
public interface ReferrerRepository extends CrudRepository<Referrer, ReferrerIdentity> {

    /**
     * List all referrers for the given article.
     *
     * @param id the ID of an article to find referrers for.
     * @return a list of referrers which referred to this article.
     */
    List<Referrer> findAllByReferrerIdentityArticleId(Integer id);
}
