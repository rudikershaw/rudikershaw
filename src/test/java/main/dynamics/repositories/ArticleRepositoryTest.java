package main.dynamics.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import main.dynamics.entities.Article;

@DataJpaTest
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testEntityCrud()
    {
        final Article article = createArticle();
        articleRepository.save(article);

        final Article loaded = articleRepository.findByPath(article.getPath());

        assertThat(loaded).isEqualTo(article);
    }

    private Article createArticle()
    {
        return new Article("Title", UUID.randomUUID().toString(), "image/path.png", "image/path.png", "Description.");
    }
}
