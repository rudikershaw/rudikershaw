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
    private final String consumerKey;

    /** Twitter API consumer secret. */
    private final String consumerSecret;

    /** Twitter API access key. */
    private final String accessKey;

    /** Twitter API access secret. */
    private final String accessSecret;

    /**
     * Provider constructor.
     *
     * @param consumerKey the Twitter consumer key
     * @param consumerSecret the Twitter consumer secret
     * @param accessKey the Twitter access key
     * @param accessSecret the Twitter access secret
     */
    public AuthDetailsProvider(@Value("${twitter.consumer.key}") final String consumerKey,
                               @Value("${twitter.consumer.secret}") final String consumerSecret,
                               @Value("${twitter.access.key}") final String accessKey,
                               @Value("${twitter.access.secret}") final String accessSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
    }

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
