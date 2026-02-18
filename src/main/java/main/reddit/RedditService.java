package main.reddit;

import java.net.URL;
import java.util.List;

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

    /** The Reddit username to fetch posts for. */
    @Value("${reddit.username:}")
    private String redditUsername;

    /**
     * Get the latest Reddit post for the configured username.
     *
     * @return the latest post as a SyndEntry, or null if unavailable.
     */
    @Cacheable("latest-reddit-post")
    public SyndEntry getLatestPost() {
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
            return entries.get(0);
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
     * Extract the media:thumbnail URL from a feed entry.
     *
     * @param entry the feed entry.
     * @return the thumbnail URL, or null.
     */
    public String getThumbnail(final SyndEntry entry) {
        if (entry == null) {
            return null;
        }
        final MediaEntryModule media = (MediaEntryModule) entry.getModule(
                MediaEntryModule.URI);
        if (media != null) {
            final Thumbnail[] thumbnails = media.getMetadata() != null
                    ? media.getMetadata().getThumbnail() : null;
            if (thumbnails != null && thumbnails.length > 0) {
                return thumbnails[0].getUrl().toString();
            }
            for (MediaGroup group : media.getMediaGroups()) {
                final Metadata groupMeta = group.getMetadata();
                if (groupMeta != null && groupMeta.getThumbnail().length > 0) {
                    return groupMeta.getThumbnail()[0].getUrl().toString();
                }
            }
        }
        return null;
    }

    /**
     * Extract plain text description from a feed entry's content.
     *
     * @param entry the feed entry.
     * @return the plain text, or null.
     */
    public String getDescription(final SyndEntry entry) {
        if (entry == null) {
            return null;
        }
        String html = null;
        if (entry.getDescription() != null) {
            html = entry.getDescription().getValue();
        } else if (entry.getContents() != null && !entry.getContents().isEmpty()) {
            html = entry.getContents().get(0).getValue();
        }
        if (html == null || html.isEmpty()) {
            return null;
        }
        // Strip HTML tags and clean up.
        String text = html.replaceAll("<[^>]+>", " ")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
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
