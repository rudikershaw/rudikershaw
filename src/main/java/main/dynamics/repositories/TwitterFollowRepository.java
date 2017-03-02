package main.dynamics.repositories;

import java.util.List;
import main.dynamics.entities.Article;
import main.dynamics.entities.TwitterFollow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Rudi Kershaw on 03/03/2017.
 */
@Transactional
public interface TwitterFollowRepository extends CrudRepository<TwitterFollow, Integer> {
    List<TwitterFollow> findAllByDisabledFalseOrderByDateDesc(Pageable pageable);
    TwitterFollow findByName(String name);
    List<TwitterFollow> findAllByDisabledFalse();
}
