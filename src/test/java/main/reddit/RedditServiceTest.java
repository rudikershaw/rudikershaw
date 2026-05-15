package main.reddit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/** Tests for the RedditService. */
public class RedditServiceTest {

    @Test
    public void testGetLatestPostReturnsNullWhenUsernameEmpty() throws Exception {
        final var service = new RedditService("");
        Assertions.assertThat(service.getLatestPost()).isNull();
    }

    @Test
    public void testGetLatestPostReturnsNullWhenUsernameNull() throws Exception {
        final var service = new RedditService(null);
        Assertions.assertThat(service.getLatestPost()).isNull();
    }

    @Test
    public void testGetLatestPostReturnsNullForInvalidUsername() throws Exception {
        final var service = new RedditService("this-user-should-not-exist-abc123xyz789");
        Assertions.assertThat(service.getLatestPost()).isNull();
    }

    @Test
    public void testInvalidateCacheDoesNotThrow() {
        final var service = new RedditService("test-username");
        service.invalidateCache();
    }

    @Test
    public void testToDtoReturnsNullForNullEntry() {
        final var service = new RedditService("test-username");
        Assertions.assertThat(service.toDto(null)).isNull();
    }

    @Test
    public void testToDtoReturnsNullForUnsafeLinkScheme() {
        final var service = new RedditService("test-username");
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("title");
        entry.setLink("javascript:alert(1)");
        Assertions.assertThat(service.toDto(entry)).isNull();
    }

    @Test
    public void testToDtoReturnsNullForNonRedditLink() {
        final var service = new RedditService("test-username");
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("title");
        entry.setLink("https://evil.example.com/post");
        Assertions.assertThat(service.toDto(entry)).isNull();
    }

    @Test
    public void testToDtoPopulatesFieldsForSafeEntry() {
        final var service = new RedditService("test-username");
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("Hello");
        entry.setLink("https://www.reddit.com/r/test/comments/abc/hello/");
        final RedditPost dto = service.toDto(entry);
        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getTitle()).isEqualTo("Hello");
        Assertions.assertThat(dto.getLink()).isEqualTo("https://www.reddit.com/r/test/comments/abc/hello/");
        Assertions.assertThat(dto.getThumbnail()).isNull();
    }
}
