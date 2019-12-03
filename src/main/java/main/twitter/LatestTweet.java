package main.twitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** POJO for the salient Tweet details needed in the view layer. */
public class LatestTweet {

    /** The ID for this Tweet. */
    private long id;

    /** The date this tweet was created. */
    private Date created;

    /** The text for the body of the tweet. */
    private String text;

    /** The base 64 string value of images attached to the tweet. */
    private String image;

    /** The number of retweets. */
    private int retweets;

    /** The number of likes. */
    private int likes;

    /** The number of replies. */
    private int replies;

    /** The author of this tweet. */
    private TwitterUser author = new TwitterUser();

    /** The screen names of those being replied to. */
    private List<String> inReplyTo = new ArrayList<>();

    /** Default constructor. */
    public LatestTweet() {
        super();
    }

    /**
     * Constructor taking the creation date and tweet text.
     *
     * @param createdDate the date the tweet was created.
     * @param tweetText the body of the tweet's text.
     * @param tweetId the ID of the tweet.
     */
    public LatestTweet(final Date createdDate, final String tweetText, final long tweetId) {
        created = new Date(createdDate.getTime());
        text = tweetText;
        id = tweetId;
    }

    /**
     * Gets the date the Tweet was created.
     *
     * @return the created date.
     */
    public Date getCreated() {
        return new Date(created.getTime());
    }

    /**
     * Sets the date the Tweet was created.
     *
     * @param tweetCreated the created date.
     */
    public void setCreated(final Date tweetCreated) {
        this.created = new Date(tweetCreated.getTime());
    }

    /**
     * Gets the text for the body of the tweet..
     *
     * @return the text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text for the body of the tweet..
     *
     * @param tweetText the text.
     */
    public void setText(final String tweetText) {
        this.text = tweetText;
    }

    /**
     * Gets the base 64 string value of images attached to the tweet.
     *
     * @return the image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the base 64 string value of images attached to the tweet.
     *
     * @param tweetImage the image.
     */
    public void setImage(final String tweetImage) {
        this.image = tweetImage;
    }

    /**
     * Gets the ID for this Tweet.
     *
     * @return the link.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID for this Tweet.
     *
     * @param tweetId the link.
     */
    public void setId(final long tweetId) {
        this.id = tweetId;
    }

    /**
     * Gets the list user screen names representing the users this tweet is a reply to.
     *
     * @return the inReplyTo.
     */
    public List<String> getInReplyTo() {
        return inReplyTo;
    }

    /**
     * Sets the list user screen names representing the users this tweet is a reply to.
     *
     * @param inReplyToList the collection of screen names.
     */
    public void setInReplyTo(final List<String> inReplyToList) {
        this.inReplyTo = inReplyToList;
    }

    /**
     * Gets the number of retweets.
     *
     * @return the retweets.
     */
    public int getRetweets() {
        return retweets;
    }

    /**
     * Sets the number of retweets.
     *
     * @param retweetsNo the retweets.
     */
    public void setRetweets(final int retweetsNo) {
        this.retweets = retweetsNo;
    }

    /**
     * Gets the number of likes.
     *
     * @return the likes.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Sets the number of likes.
     *
     * @param likesNo the likes.
     */
    public void setLikes(final int likesNo) {
        this.likes = likesNo;
    }

    /**
     * Gets the author of this tweet.
     *
     * @return the author.
     */
    public TwitterUser getAuthor() {
        return author;
    }

    /**
     * Sets the author of this tweet.
     *
     * @param user the author.
     */
    public void setAuthor(final TwitterUser user) {
        this.author = user;
    }

    /**
     * Gets the number of replies.
     *
     * @return the replies.
     */
    public int getReplies() {
        return replies;
    }

    /**
     * Sets the number of replies.
     *
     * @param repliesNo the replies.
     */
    public void setReplies(final int repliesNo) {
        this.replies = repliesNo;
    }

    /**
     * Gets the list user screen names representing the users this tweet is a reply to.
     *
     * @return the inReplyTo.
     */
    public String getFormattedInReplyTo() {
        if (inReplyTo != null && !inReplyTo.isEmpty()) {
            final StringBuilder formattedReplyTo = new StringBuilder();
            final int count = inReplyTo.size();
            for (int i = 0; i < count; i++) {
                if (i == (count - 1) && count > 1) {
                    formattedReplyTo.append(" and");
                }
                formattedReplyTo.append(" @").append(inReplyTo.get(i));
            }
            return formattedReplyTo.toString();
        } else {
            return null;
        }
    }
}
