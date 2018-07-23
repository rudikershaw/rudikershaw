package main.dynamics.entites;

import org.junit.Assert;
import org.junit.Test;

import main.dynamics.entities.Article;

public class ArticleTest {

    @Test
    public void testGetLargeImagePath() {
        final Article article = new Article();
        article.setImagePath("images/ai-doom-isnt-coming.jpg");
        Assert.assertEquals("images/ai-doom-isnt-coming-large.jpg", article.getLargeImagePath());

        article.setImagePath("images/RK.ico");
        Assert.assertEquals("images/RK.ico", article.getLargeImagePath());
    }
}
