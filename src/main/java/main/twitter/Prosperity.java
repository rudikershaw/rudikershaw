package main.twitter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import main.dynamics.entities.TwitterFollow;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/**
 * The Twitter Prosperity class runs timed tasks designed to make the twitter
 * experience of the site owner more interesting by rotating extra followers.
 */
@Component
class Prosperity {

    /** Provider for Twitter API authentication details. */
    private final AuthDetailsProvider provider;

    /** Injected twitter follow service. */
    private final TwitterFollowService service;

    /** The maximum number of tweets per day that is desirable for a new follow. */
    private static final int MAX_TWEETS_PER_DAY = 20;

    /** The maximum desirable ratio of friends to followers for a new follow. */
    private static final double FRIEND_FOLLOWERS_RATIO = 0.1;

    /** The number of tweets to scan from the timeline. */
    private static final int NO_TWEETS_FROM_TIMELINE = 200;

    /**
     * Constructor for autowiring services.
     *
     * @param twitterFollowService twitter follow service for autowiring.
     * @param authDetailsProvider provider for twitter authentication for autowiring.
     */
    @Autowired
    Prosperity(final TwitterFollowService twitterFollowService, final AuthDetailsProvider authDetailsProvider) {
        this.service = twitterFollowService;
        this.provider = authDetailsProvider;
    }

    /**
     * Run twitter follower rotating every day at 1am.
     *
     * @throws TwitterException if connection could not be made.
     */
    @Scheduled(cron = "0 1 1 * * ?")
    public void run() throws TwitterException {
        // Check that keys are set
        System.out.println("Twitter Prosperity Running");
        if (!provider.detailAvailable()) {
            return;
        }

        final TwitterFactory factory = new TwitterFactory();
        final Twitter twitter = factory.getInstance();
        final AccessToken accessToken = provider.getAccessToken();
        provider.setOAuthConsumer(twitter);
        twitter.setOAuthAccessToken(accessToken);

        final TwitterFollow latestFollow = service.getLatestFollow();

        final Date yesterday = getXDaysAgo(1);
        if (latestFollow == null || latestFollow.getDate().before(yesterday)) {
            // Get the retweets from the website owner's timeline.
            final Paging paging = new Paging();
            paging.setCount(NO_TWEETS_FROM_TIMELINE);
            List<Status> statuses = twitter.getHomeTimeline(paging);
            statuses = statuses.stream().filter(Status::isRetweet).collect(Collectors.toList());
            // Loop through and follow first desirable user you don't already
            for (final Status s : statuses) {
                final Status originalTweet = s.getRetweetedStatus();
                final User user = originalTweet.getUser();
                final String author = user.getScreenName();
                if (service.getFollowByName(author) == null && isDesirable(user, twitter)) {
                    System.out.println("Twitter Prosperity: Added follow - " + author);
                    twitter.createFriendship(author);
                    service.addFollow(new TwitterFollow(author, new Date()));
                    break;
                }
            }
        } else {
            System.out.println("Twitter Prosperity: Follow added too recently");
        }

        // Clear up followers older than 7 days.
        final List<TwitterFollow> follows = service.getFollows();
        final Date lastWeek = getXDaysAgo(7);

        for (final TwitterFollow follow : follows) {
            if (follow.getDate().before(lastWeek)) {
                // Remove follow from twitter.
                System.out.println("Twitter Prosperity: Removed follow - " + follow.getName());
                try {
                    twitter.destroyFriendship(follow.getName());
                } catch (final TwitterException e) {
                    System.out.println("Follow '" + follow.getName() + "' not found. Removing from DB and proceeding.");
                }
                // Disable follow in DB.
                service.disableFollow(follow);
            }
        }
    }

    /**
     * Checks whether the user is desirable as someone for following.
     * @param user the user to check.
     * @param twitter the twitter instance for API calls.
     * @return true if the user is desirable for following.
     * @throws TwitterException if connection could not be made.
     */
    private boolean isDesirable(final User user, final Twitter twitter) throws TwitterException {
        // Check whether user follows at least 1/10 as many people as follow them.
        final boolean goodFollowerRatio = user.getFriendsCount() >= (user.getFollowersCount() * FRIEND_FOLLOWERS_RATIO);
        // Check whether user tweeted more than 20 times in the last 24 hours.
        final List<Status> statuses = twitter.getUserTimeline(user.getScreenName());
        final Date yesterday =  getXDaysAgo(1);
        statuses.removeIf(s -> s.getCreatedAt().before(yesterday));
        final boolean tooManyTweets = statuses.size() >= MAX_TWEETS_PER_DAY;
        // Desirable if their follower ratio is good and they don't tweet too often.
        return goodFollowerRatio && !tooManyTweets;
    }

    /**
     * Get a date representing the provided number of days in the past.
     *
     * @param daysAgo the number of days in the past to get the date from.
     * @return a date in the past.
     */
    private Date getXDaysAgo(final int daysAgo) {
        final var calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -daysAgo);
        return calendar.getTime();
    }
}
