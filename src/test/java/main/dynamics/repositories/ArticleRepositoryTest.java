package main.dynamics.repositories;

import org.assertj.core.api.Assertions;
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
        final Article article = new Article("Title", "path", "image/path.png", "Description.");
        articleRepository.save(article);

        final Article loaded = articleRepository.findByPath("path");

        Assertions.assertThat(loaded).isEqualTo(article);
    }
}
