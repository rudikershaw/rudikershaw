package main;

import main.dynamics.entities.Article;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static main.IndexController.ARTICLES_PER_PAGE;

public class IndexControllerTest {

    @Test
    public void testPaginatedArticlesList()
    {
        final IndexController controller = new IndexController(null, null);

        List<Article> articles = controller.articles(0);
        Assert.assertNotNull(articles);
        Assert.assertEquals(ARTICLES_PER_PAGE, articles.size());

        // Works with integer overflow
        articles = controller.articles(Integer.MAX_VALUE);
        Assert.assertEquals(0, articles.size());

        // Works with indexes out of bounds
        articles = controller.articles(Integer.MAX_VALUE / (ARTICLES_PER_PAGE * 2));
        Assert.assertEquals(0, articles.size());

        // Works with negative numbers
        articles = controller.articles(-1);
        Assert.assertEquals(ARTICLES_PER_PAGE, articles.size());

        // Test robust (no exceptions).
        for (int i = -1; i < Integer.MAX_VALUE; i++) {
            controller.articles(i);
        }
    }
}
