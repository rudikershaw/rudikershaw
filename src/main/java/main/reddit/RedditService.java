package main.reddit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.modules.mediarss.types.MediaGroup;
import com.rometools.modules.mediarss.types.Metadata;
import com.rometools.modules.mediarss.types.Thumbnail;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

/** Service for accessing the latest Reddit post via the public RSS feed. */
@Service
public class RedditService {

    /** Logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(RedditService.class);

    /** Two minutes as a number of milliseconds. */
    private static final int TWO_MINUTES = 120000;

    /** Prefix that post links must begin with to be considered safe. */
    private static final String SAFE_LINK_PREFIX = "https://www.reddit.com/";

    /** Prefix that thumbnail URLs must begin with to be considered safe. */
    private static final String SAFE_IMAGE_PREFIX = "https://";

    /** The Reddit username to fetch posts for. */
    @Value("${reddit.username:}")
    private String redditUsername;

    /**
     * Get the latest Reddit post for the configured username as a DTO.
     *
     * @return the latest post as a {@link RedditPost}, or null if unavailable.
     */
    @Cacheable("latest-reddit-post")
    public RedditPost getLatestPost() {
        if (redditUsername == null || redditUsername.isEmpty()) {
            LOG.warn("Reddit username is not configured.");
            return null;
        }

        try {
            final String rssUrl = "https://www.reddit.com/user/"
                    + redditUsername + "/submitted.rss";
            final List<SyndEntry> entries = new SyndFeedInput()
                    .build(new XmlReader(new URL(rssUrl))).getEntries();

            if (entries == null || entries.isEmpty()) {
                LOG.info("No entries found in Reddit RSS feed.");
                return null;
            }
            return toDto(entries.get(0));
        } catch (Exception e) {
            LOG.error("Failed to fetch latest Reddit post.", e);
            return null;
        }
    }

    /** Evict entries from the Latest Reddit Post cache every 120 seconds. */
    @CacheEvict(allEntries = true, cacheNames = { "latest-reddit-post" })
    @Scheduled(fixedDelay = TWO_MINUTES)
    public void invalidateCache() {
        // The effect of this method is purely in the annotations.
    }

    /**
     * Convert a feed entry into a {@link RedditPost} DTO suitable for the
     * browser. Only posts with a safe reddit.com https link are returned so
     * that a compromised or unexpected feed cannot inject arbitrary link
     * schemes (e.g. javascript:) into the page.
     *
     * @param entry the feed entry.
     * @return the DTO, or null if the entry cannot be converted safely.
     */
    RedditPost toDto(final SyndEntry entry) {
        if (entry == null) {
            return null;
        }
        final String link = entry.getLink();
        if (link == null || !link.startsWith(SAFE_LINK_PREFIX)) {
            LOG.info("Skipping Reddit entry with unsafe or missing link.");
            return null;
        }
        return new RedditPost(
                extractSubreddit(entry),
                extractPublished(entry),
                entry.getTitle(),
                extractDescription(entry),
                link,
                extractThumbnail(entry));
    }

    /**
     * Extract a safe thumbnail URL from the entry's MediaRSS module. Only
     * https URLs are returned so that no unexpected scheme can end up in an
     * img src attribute in the browser.
     *
     * @param entry the feed entry.
     * @return the thumbnail URL, or null.
     */
    private String extractThumbnail(final SyndEntry entry) {
        final MediaEntryModule media = (MediaEntryModule) entry.getModule(
                MediaEntryModule.URI);
        if (media == null) {
            return null;
        }
        final Metadata topMeta = media.getMetadata();
        final Thumbnail[] topThumbs = topMeta != null ? topMeta.getThumbnail() : null;
        if (topThumbs != null && topThumbs.length > 0) {
            final String url = topThumbs[0].getUrl().toString();
            if (url.startsWith(SAFE_IMAGE_PREFIX)) {
                return url;
            }
        }
        for (MediaGroup group : media.getMediaGroups()) {
            final Metadata groupMeta = group.getMetadata();
            if (groupMeta != null && groupMeta.getThumbnail().length > 0) {
                final String url = groupMeta.getThumbnail()[0].getUrl().toString();
                if (url.startsWith(SAFE_IMAGE_PREFIX)) {
                    return url;
                }
            }
        }
        return null;
    }

    /**
     * Extract the subreddit name (without the "r/" prefix) from the first
     * category on the entry.
     *
     * @param entry the feed entry.
     * @return the subreddit name, or null.
     */
    private String extractSubreddit(final SyndEntry entry) {
        final List<SyndCategory> categories = entry.getCategories();
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return categories.get(0).getName();
    }

    /**
     * Format the entry's published date as "MMM dd".
     *
     * @param entry the feed entry.
     * @return the formatted date, or null.
     */
    private String extractPublished(final SyndEntry entry) {
        if (entry.getPublishedDate() == null) {
            return null;
        }
        return new SimpleDateFormat("MMM dd", Locale.ENGLISH)
                .format(entry.getPublishedDate());
    }

    /**
     * Extract plain text description from a feed entry's content.
     *
     * @param entry the feed entry.
     * @return the plain text, or null.
     */
    private String extractDescription(final SyndEntry entry) {
        String html = null;
        if (entry.getDescription() != null) {
            html = entry.getDescription().getValue();
        } else if (entry.getContents() != null && !entry.getContents().isEmpty()) {
            html = entry.getContents().get(0).getValue();
        }
        if (html == null || html.isEmpty()) {
            return null;
        }
        // Strip HTML tags and clean up. Output is rendered via textContent in
        // the browser, so this is for readability rather than safety.
        String text = html.replaceAll("<[^>]+>", " ")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&#32;", "")
                .replace("&nbsp;", " ")
                .replaceAll("\\s+", " ")
                .trim();
        // Remove the "submitted by... [link] [comments]" boilerplate.
        final int submittedIdx = text.indexOf("submitted by");
        if (submittedIdx > 0) {
            text = text.substring(0, submittedIdx).trim();
        }
        return text.isEmpty() ? null : text;
    }
}
