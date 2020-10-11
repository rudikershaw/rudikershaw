package main.dynamics.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.dynamics.entities.Article;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
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
        return new Article("Title", UUID.randomUUID().toString(), "image/path.png", "Description.");
    }
}
