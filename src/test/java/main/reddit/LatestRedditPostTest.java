package main.reddit;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/** Tests for the LatestRedditPost POJO. */
public class LatestRedditPostTest {

    @Test
    public void testGettersAndSetters() {
        final LatestRedditPost post = new LatestRedditPost();

        post.setTitle("Test Title");
        Assert.assertEquals("Test Title", post.getTitle());

        post.setSelftext("Some body text");
        Assert.assertEquals("Some body text", post.getSelftext());

        post.setSubreddit("r/java");
        Assert.assertEquals("r/java", post.getSubreddit());

        post.setAuthor("testuser");
        Assert.assertEquals("testuser", post.getAuthor());

        post.setPermalink("/r/java/comments/abc123/test/");
        Assert.assertEquals("/r/java/comments/abc123/test/", post.getPermalink());

        post.setScore(42);
        Assert.assertEquals(42, post.getScore());

        post.setNumComments(7);
        Assert.assertEquals(7, post.getNumComments());

        post.setThumbnail("https://example.com/thumb.jpg");
        Assert.assertEquals("https://example.com/thumb.jpg", post.getThumbnail());
    }

    @Test
    public void testCreatedDateDefensiveCopy() {
        final LatestRedditPost post = new LatestRedditPost();
        final Date date = new Date();

        post.setCreated(date);
        final Date retrieved = post.getCreated();

        Assert.assertEquals(date.getTime(), retrieved.getTime());
        Assert.assertNotSame(date, retrieved);

        // Mutating the original should not affect the stored value.
        date.setTime(0);
        Assert.assertNotEquals(0, post.getCreated().getTime());
    }

    @Test
    public void testCreatedDateNull() {
        final LatestRedditPost post = new LatestRedditPost();
        post.setCreated(null);
        Assert.assertNull(post.getCreated());
    }

    @Test
    public void testDefaultValues() {
        final LatestRedditPost post = new LatestRedditPost();
        Assert.assertNull(post.getTitle());
        Assert.assertNull(post.getSelftext());
        Assert.assertNull(post.getSubreddit());
        Assert.assertNull(post.getAuthor());
        Assert.assertNull(post.getPermalink());
        Assert.assertNull(post.getThumbnail());
        Assert.assertNull(post.getCreated());
        Assert.assertEquals(0, post.getScore());
        Assert.assertEquals(0, post.getNumComments());
    }
}
