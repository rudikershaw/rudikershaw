package main.twitter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.IMAGE_GIF;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_PNG;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.auth.AccessToken;

/** Service for accessing twitter information using the Twitter4j API. */
@Service
public class TwitterService {

    /** Two minute as a number of milliseconds. */
    private static final int TWO_MINUTES = 120000;

    /** Twitter object for access to Twitter4j API. */
    private final Twitter twitter;

    /**
     * Constructor for dependency injection.
     *
     * @param authDetailsProvider an AuthDetailsProvider.
     */
    @Autowired
    public TwitterService(final AuthDetailsProvider authDetailsProvider) {
        final var factory = new TwitterFactory();
        twitter = factory.getInstance();
        final AccessToken accessToken = authDetailsProvider.getAccessToken();
        authDetailsProvider.setOAuthConsumer(twitter);
        twitter.setOAuthAccessToken(accessToken);
    }

    /**
     * Get an object representing my latest tweet.
     *
     * @return my latest tweet.
     */
    @Cacheable("latest-tweet")
    public LatestTweet getMyLatestTweet() {
        try {
            final int tweetsToGet = 20;
            final Status status = twitter.getUserTimeline(new Paging(1, tweetsToGet)).stream()
                                              .filter(s -> !s.isRetweet()).findFirst().orElse(null);
            if (status != null) {
                final LatestTweet latestTweet = new LatestTweet(status.getCreatedAt(), status.getText(), status.getId());
                latestTweet.setRetweets(status.getRetweetCount());
                latestTweet.setLikes(status.getFavoriteCount());
                latestTweet.setAuthor(setUserDetails(latestTweet.getAuthor(), status.getUser()));

                final MediaEntity[] mediaEntities = status.getMediaEntities();
                processReplies(latestTweet, status.getUserMentionEntities());
                replaceUrlsEntities(latestTweet, status.getURLEntities());

                if (mediaEntities != null && mediaEntities.length > 0) {
                    final MediaEntity mediaEntity = mediaEntities[0];
                    latestTweet.setImage(getBase64ImageString(mediaEntity.getMediaURLHttps()));
                    removeMediaUrls(latestTweet, mediaEntities);
                }
                return latestTweet;
            } else {
                return null;
            }
        } catch (TwitterException e) {
            return null;
        }
    }

    /** Evict entries from the Latest Tweet cache every 120 seconds. */
    @CacheEvict(allEntries = true, cacheNames = { "latest-tweet" })
    @Scheduled(fixedDelay = TWO_MINUTES)
    public void invalidateCache() {
        // The effect of this method is purely in the annotations.
    }

    /**
     * Attempts to download an image from the provided URL and converts it to a base 64 String.
     *
     * @param url the URL to get the image from.
     * @return a base 64 String or null.
     */
    private String getBase64ImageString(final String url) {
        final var headers = new HttpHeaders();
        headers.setAccept(List.of(IMAGE_GIF, IMAGE_JPEG, IMAGE_PNG));
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        final ResponseEntity<byte[]> response = new RestTemplate().exchange(url, GET, entity, byte[].class);

        if (!response.getStatusCode().isError()) {
            return Base64.getEncoder().encodeToString(response.getBody());
        } else {
            return null;
        }
    }

    /**
     * Replace twitteresque URLs from Tweet text and replace with the real displayed URL.
     *
     * @param tweet the tweet who's text needs amending.
     * @param entities the URL entities registered for this tweet.
     */
    private void replaceUrlsEntities(final LatestTweet tweet, final URLEntity[] entities) {
        if (entities != null) {
            String text = tweet.getText();
            for (URLEntity entity : entities) {
                text = text.replace(entity.getURL(), entity.getDisplayURL()).strip();
            }
            tweet.setText(text);
        }
    }

    /**
     * Remove all media URLs from Tweet text.
     *
     * @param tweet the tweet who's text needs amending.
     * @param entities the media entities registered for this tweet.
     */
    private void removeMediaUrls(final LatestTweet tweet, final MediaEntity[] entities) {
        if (entities != null) {
            String text = tweet.getText();
            for (MediaEntity entity : entities) {
                text = text.replace(entity.getURL(), "").strip();
            }
            tweet.setText(text);
        }
    }

    /**
     * Remove replies from beginning of tweet text and add them as replies to seperate field on the LatestTweet object.
     *
     * @param tweet the latest tweet to process.
     * @param mentionEntities the user mentions to check for replies.
     */
    private void processReplies(final LatestTweet tweet, final UserMentionEntity[] mentionEntities) {
        String text = tweet.getText();
        if (mentionEntities != null && mentionEntities.length > 0) {
            while (startsWithAReply(text, mentionEntities)) {
                for (UserMentionEntity mention : mentionEntities) {
                    if (text.startsWith("@" + mention.getScreenName())) {
                        text = text.replace("@" + mention.getScreenName(), "").strip();
                        tweet.getInReplyTo().add(mention.getScreenName());
                    }
                }
            }
        }
        tweet.setText(text);
    }

    /**
     * Checks whether a tweet text starts with a reply.
     *
     * @param text the tweet text to check.
     * @param mentionEntities the user mentions to validate against.
     * @return true if the text start with a reply, else false.
     */
    private boolean startsWithAReply(final String text, final UserMentionEntity[] mentionEntities) {
        for (UserMentionEntity mention : mentionEntities) {
            if (text.startsWith("@" + mention.getScreenName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Sets details of the TwitterUser POJO using the user retrieved from the latest tweet.
     *
     * @param twitterUser the twitter user to populate.
     * @param user the Twitter4j user to get the details from.
     * @return the provided TwitterUser.
     */
    private TwitterUser setUserDetails(final TwitterUser twitterUser, final User user) {
        twitterUser.setImage(getBase64ImageString(user.getProfileImageURL()));
        twitterUser.setName(user.getName());
        twitterUser.setScreenName(user.getScreenName());
        return twitterUser;
    }
}
