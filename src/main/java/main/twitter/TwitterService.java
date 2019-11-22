package main.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/** Service for accessing twitter information using the Twitter4j API. */
@Service
public class TwitterService {

    /** Twitter object for access to Twitter4j API. */
    private final Twitter twitter;

    /**
     * Constructor for dependency injection.
     *
     * @param authDetailsProvider an AuthDetailsProvider.
     */
    @Autowired
    public TwitterService(final AuthDetailsProvider authDetailsProvider) {
        final TwitterFactory factory = new TwitterFactory();
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
    public Status getMyLatestTweet() {
        try {
            return twitter.getUserTimeline(new Paging(1, 1)).get(0);
        } catch (TwitterException e) {
            return null;
        }
    }

    /** Evict entries from the Latest Tweet cache every 30 seconds. */
    @CacheEvict(allEntries = true, cacheNames = { "latest-tweet" })
    @Scheduled(fixedDelay = 30000)
    public void invalidateCache () {
        // The effect of this method is purely in the annotations.
    }
}
