package main.twitter;

import java.util.Date;

/** POJO for the salient Tweet details needed in the view layer. */
public class LatestTweet {

    /** The date this tweet was created. */
    private Date created;

    /** The text for the body of the tweet. */
    private String text;

    /** The base 64 string value of images attached to the tweet. */
    private String image;

    /** The ID for this Tweet. */
    private long id;

    /** Default constructor. */
    public LatestTweet() {
        super();
    }

    /**]
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
}
