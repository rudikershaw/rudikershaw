package main.reddit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class RedditPostTest {

    @Test
    public void testGettersReturnConstructorValues() {
        final RedditPost post = new RedditPost("testsub", "Jun 12", "Test Title",
            "Test description", "https://reddit.com/r/test", "https://example.com/thumb.png");

        assertThat(post.getSubreddit()).isEqualTo("testsub");
        assertThat(post.getPublished()).isEqualTo("Jun 12");
        assertThat(post.getTitle()).isEqualTo("Test Title");
        assertThat(post.getDescription()).isEqualTo("Test description");
        assertThat(post.getLink()).isEqualTo("https://reddit.com/r/test");
        assertThat(post.getThumbnail()).isEqualTo("https://example.com/thumb.png");
    }

    @Test
    public void testGettersHandleNullValues() {
        final RedditPost post = new RedditPost(null, null, "Title", null, "https://example.com", null);

        assertThat(post.getSubreddit()).isNull();
        assertThat(post.getPublished()).isNull();
        assertThat(post.getTitle()).isEqualTo("Title");
        assertThat(post.getDescription()).isNull();
        assertThat(post.getLink()).isEqualTo("https://example.com");
        assertThat(post.getThumbnail()).isNull();
    }
}
