package main.dynamics.repositories;

import main.dynamics.entities.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Spring CRUD repository for articles. */
@Transactional
public interface ArticleRepository extends CrudRepository<Article, Integer> {

    /**
     * Find all articles ordered by most recently published.
     * @param pageable the paging information for the requested articles.
     * @return a list of Article entities.
     */
    List<Article> findAllByOrderByPublishedDesc(Pageable pageable);

    /**
     * Find an article by view path.
     * @param path the path of the article to find's view.
     * @return an Article entity.
     */
    Article findByPath(String path);
}
