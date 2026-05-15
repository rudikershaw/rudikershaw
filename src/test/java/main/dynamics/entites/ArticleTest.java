package main.dynamics.entites;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import main.dynamics.entities.Article;

public class ArticleTest {

    @Test
    public void testGetLargeImagePath() {
        final Article article = new Article();
        article.setImagePath("images/ai-doom-isnt-coming.jpg");
        Assertions.assertThat(article.getLargeImagePath()).isEqualTo("images/ai-doom-isnt-coming-large.jpg");

        article.setImagePath("images/RK.ico");
        Assertions.assertThat(article.getLargeImagePath()).isEqualTo("images/RK.ico");
    }
}
