package main.dynamics.entites;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.dynamics.entities.Article;

public class ArticleTest {

    @Test
    public void testGetLargeImagePath() {
        final Article article = new Article();
        article.setImagePath("images/ai-doom-isnt-coming.jpg");
        assertThat(article.getLargeImagePath()).isEqualTo("images/ai-doom-isnt-coming-large.jpg");

        article.setImagePath("images/RK.ico");
        assertThat(article.getLargeImagePath()).isEqualTo("images/RK.ico");
    }
}
