package main.twitter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import main.dynamics.TwitterFollowService;
import main.dynamics.entities.TwitterFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * The Twitter Prosperity class runs timed tasks designed to make the twitter
 * experience of the site owner more interesting by rotating extra followers.
 */
@Component
public class Prosperity {

    @Value("${twitter.consumer.key")
    private String consumerKey;

    @Value("${twitter.consumer.secret")
    private String consumerSecret;

    @Value("${twitter.access.key")
    private String accessKey;

    @Value("${twitter.access.secret")
    private String accessSecret;

    private TwitterFollowService service;

    @Autowired
    public Prosperity(TwitterFollowService service) {
        this.service = service;
    }

    /** Run twitter follower rotating every day and one hour. */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 25)
    public void run() throws TwitterException {
        // Check that keys are set
        if (consumerSecret.startsWith("${")) {
            return;
        }

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        AccessToken accessToken = new AccessToken(accessKey, accessSecret);
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(accessToken);

        TwitterFollow latestFollow = service.getLatestFollow();
        Date yesterday = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
        if (latestFollow == null || latestFollow.getDate().before(yesterday)) {
            // Get the retweets and x favorited y statuses
            Paging paging = new Paging();
            paging.setCount(200);
            List<Status> statuses = twitter.getHomeTimeline(paging);
            statuses = statuses.stream().filter(s -> !s.isRetweet()).collect(Collectors.toList());
            // Loop through. Follow first person you don't
            for (Status s : statuses) {
                Status originalTweet = s.getRetweetedStatus();
                String author = originalTweet.getUser().getName();
                if (service.getFollowByName(author) == null) {
                    twitter.createFriendship(author);
                    service.addFollow(new TwitterFollow(author, new Date()));
                    break;
                }
            }
        }

        // Clear up followers older than 7 days.
        List<TwitterFollow> follows = service.getFollows();
        Date lastWeek = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 7));
        for (TwitterFollow follow : follows) {
            if (follow.getDate().before(lastWeek)) {
                // Remove follow from twitter.
                twitter.destroyFriendship(follow.getName());
                // Disable follow in DB.
                service.disableFollow(follow);
            }
        }
    }
}
