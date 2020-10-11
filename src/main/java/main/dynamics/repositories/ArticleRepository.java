package main.dynamics.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.dynamics.entities.Article;

/** Spring CRUD repository for articles. */
@Repository
@Transactional
public interface ArticleRepository extends CrudRepository<Article, Integer> {

    /**
     * Find an article by view path.
     * @param path the path of the article to find's view.
     * @return an Article entity.
     */
    Article findByPath(String path);
}
