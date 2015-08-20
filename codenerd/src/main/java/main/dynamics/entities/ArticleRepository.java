package main.dynamics.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rudi Kershaw on 16/08/2015.
 */
@Transactional
public interface ArticleRepository extends CrudRepository<Article, Integer> {
    List<Article> findAllByOrderByPublishedDesc(Pageable pageable);
    Article findByPath(String path);
}
