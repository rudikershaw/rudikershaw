package main.dynamics.repositories;

import main.dynamics.entities.TwitterFollow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Spring CRUD repository for TwitterFollow entities. */
@Transactional
public interface TwitterFollowRepository extends CrudRepository<TwitterFollow, Integer> {

    /**
     * Find all enabled twitter follows ordered latest first.
     * @param pageable configuration for the how many you want etc.
     * @return a list of twitter follows.
     */
    List<TwitterFollow> findAllByDisabledFalseOrderByDateDesc(Pageable pageable);

    /**
     * Find a twitter follow by user name.
     * @param name the username of the follow.
     * @return the TwitterFollow.
     */
    TwitterFollow findByName(String name);

    /**
     * Find all enabled follows.
     * @return a list of all enabled twitter follows.
     */
    List<TwitterFollow> findAllByDisabledFalse();
}
