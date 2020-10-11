package main.dynamics.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.dynamics.entities.Article;
import main.dynamics.entities.Referer;
import main.dynamics.entities.RefererIdentity;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RefererRepositoryTest {

    private Article article;

    @Autowired
    private RefererRepository refererRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Before
    public void setup() {
        article = new Article("Title", UUID.randomUUID().toString(), "image/path.png", "Description.");
        articleRepository.save(article);
    }

    @Test
    public void testRefererCrud() {
        final Referer referer = createReferer();
        refererRepository.save(referer);

        final List<Referer> loadedReferers = refererRepository.findAllByRefererIdentityArticleId(article.getId());

        assertThat(loadedReferers).contains(referer);
    }

    private Referer createReferer()
    {
        final RefererIdentity refererIdentity = new RefererIdentity(article, "weird-device");
        return new Referer(refererIdentity);
    }
}
