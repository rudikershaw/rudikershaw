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

    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    @Value("${twitter.access.key}")
    private String accessKey;

    @Value("${twitter.access.secret}")
    private String accessSecret;

    private TwitterFollowService service;

    @Autowired
    public Prosperity(TwitterFollowService service) {
        this.service = service;
    }

    /** Run twitter follower rotating every day at 1am. */
    @Scheduled(cron = "0 1 1 * * ?")
    public void run() throws TwitterException {
        // Check that keys are set
        System.out.println("Twitter Prosperity Running");
        if (consumerSecret.equals("fallback") || consumerKey.equals("fallback")) {
            return;
        }

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        AccessToken accessToken = new AccessToken(accessKey, accessSecret);
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(accessToken);

        TwitterFollow latestFollow = service.getLatestFollow();
        Date yesterday = new Date(new Date().getTime() - (1000 * 60 * 60 * 24) - (1000 * 60));
        if (latestFollow == null || latestFollow.getDate().before(yesterday)) {
            // Get the retweets and x favorited y statuses
            Paging paging = new Paging();
            paging.setCount(200);
            List<Status> statuses = twitter.getHomeTimeline(paging);
            statuses = statuses.stream().filter(Status::isRetweet).collect(Collectors.toList());
            // Loop through. Follow first person you don't
            for (Status s : statuses) {
                Status originalTweet = s.getRetweetedStatus();
                String author = originalTweet.getUser().getScreenName();
                if (service.getFollowByName(author) == null) {
                    System.out.println("Twitter Prosperity: Added follow - " + author);
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
                System.out.println("Twitter Prosperity: Removed follow - " + follow.getName());
                twitter.destroyFriendship(follow.getName());
                // Disable follow in DB.
                service.disableFollow(follow);
            }
        }
    }
}
