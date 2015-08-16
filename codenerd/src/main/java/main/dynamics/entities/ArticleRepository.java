package main.dynamics.entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rudi Kershaw on 16/08/2015.
 */
@Transactional
public interface ArticleRepository extends CrudRepository<Article, Integer> {
    List<Article> findAll();
    Article findByPath(String path);
}
