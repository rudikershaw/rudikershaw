package main.reddit;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

/** Tests for the RedditService. */
public class RedditServiceTest {

    @Test
    public void testGetLatestPostReturnsNullWhenUsernameEmpty() throws Exception {
        final RedditService service = new RedditService();
        setUsername(service, "");
        Assert.assertNull(service.getLatestPost());
    }

    @Test
    public void testGetLatestPostReturnsNullWhenUsernameNull() throws Exception {
        final RedditService service = new RedditService();
        setUsername(service, null);
        Assert.assertNull(service.getLatestPost());
    }

    @Test
    public void testGetLatestPostReturnsNullForInvalidUsername() throws Exception {
        final RedditService service = new RedditService();
        setUsername(service, "this-user-should-not-exist-abc123xyz789");
        Assert.assertNull(service.getLatestPost());
    }

    @Test
    public void testInvalidateCacheDoesNotThrow() {
        final RedditService service = new RedditService();
        service.invalidateCache();
    }

    @Test
    public void testToDtoReturnsNullForNullEntry() {
        final RedditService service = new RedditService();
        Assert.assertNull(service.toDto(null));
    }

    @Test
    public void testToDtoReturnsNullForUnsafeLinkScheme() {
        final RedditService service = new RedditService();
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("title");
        entry.setLink("javascript:alert(1)");
        Assert.assertNull(service.toDto(entry));
    }

    @Test
    public void testToDtoReturnsNullForNonRedditLink() {
        final RedditService service = new RedditService();
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("title");
        entry.setLink("https://evil.example.com/post");
        Assert.assertNull(service.toDto(entry));
    }

    @Test
    public void testToDtoPopulatesFieldsForSafeEntry() {
        final RedditService service = new RedditService();
        final com.rometools.rome.feed.synd.SyndEntry entry =
                new com.rometools.rome.feed.synd.SyndEntryImpl();
        entry.setTitle("Hello");
        entry.setLink("https://www.reddit.com/r/test/comments/abc/hello/");
        final RedditPost dto = service.toDto(entry);
        Assert.assertNotNull(dto);
        Assert.assertEquals("Hello", dto.getTitle());
        Assert.assertEquals("https://www.reddit.com/r/test/comments/abc/hello/",
                dto.getLink());
        Assert.assertNull(dto.getThumbnail());
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
