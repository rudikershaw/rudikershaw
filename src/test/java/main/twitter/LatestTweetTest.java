package main.twitter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LatestTweetTest {

    @Test
    public void testDefaultConstructorInitializesDefaults() {
        final LatestTweet tweet = new LatestTweet();
        assertThat(tweet.getId()).isZero();
        assertThat(tweet.getText()).isNull();
        assertThat(tweet.getRetweets()).isZero();
        assertThat(tweet.getLikes()).isZero();
        assertThat(tweet.getReplies()).isZero();
        assertThat(tweet.getAuthor()).isNotNull();
        assertThat(tweet.getInReplyTo()).isEmpty();
    }

    @Test
    public void testParameterizedConstructorSetsValues() {
        final Date date = new Date();
        final LatestTweet tweet = new LatestTweet(date, "Hello world", 12345L);

        assertThat(tweet.getId()).isEqualTo(12345L);
        assertThat(tweet.getText()).isEqualTo("Hello world");
        assertThat(tweet.getCreated()).isEqualTo(date);
    }

    @Test
    public void testSettersAndGetters() {
        final LatestTweet tweet = new LatestTweet();
        tweet.setId(99L);
        tweet.setText("Updated text");
        tweet.setRetweets(10);
        tweet.setLikes(20);
        tweet.setReplies(5);
        tweet.setImage("base64data");

        assertThat(tweet.getId()).isEqualTo(99L);
        assertThat(tweet.getText()).isEqualTo("Updated text");
        assertThat(tweet.getRetweets()).isEqualTo(10);
        assertThat(tweet.getLikes()).isEqualTo(20);
        assertThat(tweet.getReplies()).isEqualTo(5);
        assertThat(tweet.getImage()).isEqualTo("base64data");
    }

    @Test
    public void testSetInReplyTo() {
        final LatestTweet tweet = new LatestTweet();
        tweet.setInReplyTo(List.of("user1", "user2"));

        assertThat(tweet.getInReplyTo()).containsExactly("user1", "user2");
    }

    @Test
    public void testGetCreatedReturnsDefensiveCopy() {
        final Date date = new Date();
        final LatestTweet tweet = new LatestTweet(date, "text", 1L);

        final Date returned = tweet.getCreated();
        returned.setTime(0);
        assertThat(tweet.getCreated()).isEqualTo(date);
    }
}
