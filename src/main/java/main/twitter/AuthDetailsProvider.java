package main.twitter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;

/** Provider for Twitter4j API authentication details. */
@Component
public class AuthDetailsProvider {
    /** Default property if details are missing. */
    private static final String FALLBACK = "fallback";

    /** Twitter API consumer key. */
    @Value("${twitter.consumer.key}")
    private String consumerKey;

    /** Twitter API consumer secret. */
    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    /** Twitter API access key. */
    @Value("${twitter.access.key}")
    private String accessKey;

    /** Twitter API access secret. */
    @Value("${twitter.access.secret}")
    private String accessSecret;

    /**
     * Checks whether details are available for accessing Twitter4j API.
     *
     * @return true if the required details are available.
     */
    public boolean detailAvailable() {
        return !FALLBACK.equals(consumerSecret) && !FALLBACK.equals(consumerKey)
            && !FALLBACK.equals(accessSecret) && !FALLBACK.equals(accessKey);
    }

    /**
     * Get an access token for use by the Twitter4j API.
     *
     * @return an access token.
     */
    public AccessToken getAccessToken() {
        return new AccessToken(accessKey, accessSecret);
    }

    /**
     * Set the OAuthConsumer credentials on your Twitter instance.
     *
     * @param twitter Twitter instance.
     */
    public void setOAuthConsumer(final Twitter twitter) {
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }
}
