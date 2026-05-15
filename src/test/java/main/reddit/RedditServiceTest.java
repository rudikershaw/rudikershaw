package main.reddit;

import java.lang.reflect.Field;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/** Tests for the RedditService. */
public class RedditServiceTest {

    @Test
    public void testGetLatestPostReturnsNullWhenUsernameEmpty() throws Exception {
        final RedditService service = new RedditService();
        setUsername(service, "");
        Assertions.assertThat(service.getLatestPost()).isNull();
    }

    @Test
    public void testGetLatestPostReturnsNullWhenUsernameNull() throws Exception {
        final RedditService service = new RedditService();
        setUsername(service, null);
        Assertions.assertThat(service.getLatestPost()).isNull();
    }

    @Test
    public void testGetLatestPostReturnsNullForInvalidUsername() throws Exception {
        final RedditService service = new RedditService();
        setUsername(service, "this-user-should-not-exist-abc123xyz789");
        Assertions.assertThat(service.getLatestPost()).isNull();
    }

    @Test
    public void testInvalidateCacheDoesNotThrow() {
        final RedditService service = new RedditService();
        service.invalidateCache();
    }

    @Test
    public void testToDtoReturnsNullForNullEntry() {
        final RedditService service = new RedditService();
        Assertions.assertThat(service.toDto(null)).isNull();
    }

    @Test
    public void testToDtoReturnsNullForUnsafeLinkScheme() {
        final RedditService service = new RedditService();
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("title");
        entry.setLink("javascript:alert(1)");
        Assertions.assertThat(service.toDto(entry)).isNull();
    }

    @Test
    public void testToDtoReturnsNullForNonRedditLink() {
        final RedditService service = new RedditService();
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("title");
        entry.setLink("https://evil.example.com/post");
        Assertions.assertThat(service.toDto(entry)).isNull();
    }

    @Test
    public void testToDtoPopulatesFieldsForSafeEntry() {
        final RedditService service = new RedditService();
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

    /**
     * Helper to set the private redditUsername field via reflection.
     *
     * @param service the service instance.
     * @param username the username to set.
     * @throws Exception if reflection fails.
     */
    private void setUsername(final RedditService service, final String username) throws Exception {
        final Field field = RedditService.class.getDeclaredField("redditUsername");
        field.setAccessible(true);
        field.set(service, username);
    }
}
