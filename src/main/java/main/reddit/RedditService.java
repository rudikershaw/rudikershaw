package main.reddit;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Service for accessing the latest Reddit post for a configured user. */
@Service
public class RedditService {

    /** Two minutes as a number of milliseconds. */
    private static final int TWO_MINUTES = 120000;

    /** Multiplier to convert Unix epoch seconds to milliseconds. */
    private static final int MILLIS = 1000;

    /** Maximum length of selftext to display. */
    private static final int MAX_SELFTEXT_LENGTH = 300;

    /** The Reddit username to fetch posts for. */
    @Value("${reddit.username:}")
    private String redditUsername;

    /**
     * Get the latest Reddit post for the configured user.
     *
     * @return the latest Reddit post, or null if unavailable.
     */
    @Cacheable("latest-reddit-post")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public LatestRedditPost getLatestPost() {
        if (redditUsername == null || redditUsername.isEmpty()) {
            return null;
        }

        try {
            final String url = "https://www.reddit.com/user/" + redditUsername + "/submitted.json?limit=1&sort=new";
            final HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "web:codenerd:v1.0.0");
            final HttpEntity<String> entity = new HttpEntity<>(headers);
            final RestTemplate restTemplate = new RestTemplate();

            final ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class); //NOPMD

            final Map<String, Object> body = response.getBody();
            if (response.getStatusCode().isError() || body == null) {
                return null;
            }
            final Map<String, Object> data = (Map<String, Object>) body.get("data");
            final List<Map<String, Object>> children = (List<Map<String, Object>>) data.get("children");

            if (children == null || children.isEmpty()) {
                return null;
            }

            final Map<String, Object> postData = (Map<String, Object>) children.get(0).get("data");
            return mapToLatestRedditPost(postData);
        } catch (Exception e) {
            return null;
        }
    }

    /** Evict entries from the Latest Reddit Post cache every 120 seconds. */
    @CacheEvict(allEntries = true, cacheNames = {"latest-reddit-post"})
    @Scheduled(fixedDelay = TWO_MINUTES)
    public void invalidateCache() {
        // The effect of this method is purely in the annotations.
    }

    /**
     * Maps the raw Reddit API response data to a LatestRedditPost.
     *
     * @param postData the post data map from the Reddit API.
     * @return a populated LatestRedditPost.
     */
    private LatestRedditPost mapToLatestRedditPost(final Map<String, Object> postData) {
        final LatestRedditPost post = new LatestRedditPost();
        post.setTitle((String) postData.get("title"));
        post.setSubreddit((String) postData.get("subreddit_name_prefixed"));
        post.setAuthor((String) postData.get("author"));
        post.setPermalink((String) postData.get("permalink"));
        post.setScore(((Number) postData.get("score")).intValue());
        post.setNumComments(((Number) postData.get("num_comments")).intValue());

        final Number createdUtc = (Number) postData.get("created_utc");
        if (createdUtc != null) {
            post.setCreated(new Date(createdUtc.longValue() * MILLIS));
        }

        final String selftext = (String) postData.get("selftext");
        if (selftext != null && !selftext.isEmpty()) {
            if (selftext.length() > MAX_SELFTEXT_LENGTH) {
                post.setSelftext(selftext.substring(0, MAX_SELFTEXT_LENGTH) + "...");
            } else {
                post.setSelftext(selftext);
            }
        }

        final String thumbnail = (String) postData.get("thumbnail");
        if (thumbnail != null && thumbnail.startsWith("http")) {
            post.setThumbnail(extractBestImageUrl(postData));
        }

        return post;
    }

    /**
     * Extracts a suitable image URL from the Reddit post data.
     * For gallery posts uses gallery_data ordering with media_metadata, otherwise uses the preview object.
     *
     * @param postData the post data map from the Reddit API.
     * @return a suitable image URL, or null.
     */
    @SuppressWarnings("unchecked")
    private String extractBestImageUrl(final Map<String, Object> postData) {
        // Gallery posts store images in media_metadata, ordered by gallery_data.
        final Map<String, Object> galleryData = (Map<String, Object>) postData.get("gallery_data");
        final Map<String, Map<String, Object>> mediaMeta =
                (Map<String, Map<String, Object>>) postData.get("media_metadata");
        if (galleryData != null && mediaMeta != null) {
            final List<Map<String, Object>> items = (List<Map<String, Object>>) galleryData.get("items");
            if (items != null && !items.isEmpty()) {
                final String firstMediaId = (String) items.get(0).get("media_id");
                final Map<String, Object> firstImage = mediaMeta.get(firstMediaId);
                if (firstImage != null) {
                    final Map<String, Object> source = (Map<String, Object>) firstImage.get("s");
                    if (source != null && source.get("u") != null) {
                        return ((String) source.get("u")).replace("&amp;", "&");
                    }
                }
            }
        }

        // Non-gallery posts with images use the preview object.
        final Map<String, Object> preview = (Map<String, Object>) postData.get("preview");
        if (preview != null) {
            final List<Map<String, Object>> images = (List<Map<String, Object>>) preview.get("images");
            if (images != null && !images.isEmpty()) {
                final Map<String, Object> source = (Map<String, Object>) images.get(0).get("source");
                if (source != null && source.get("url") != null) {
                    return ((String) source.get("url")).replace("&amp;", "&");
                }
            }
        }

        return null;
    }
}
