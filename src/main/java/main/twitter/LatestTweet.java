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

    /**
     * Constructor taking the creation date and tweet text.
     *
     * @param createdDate the date the tweet was created.
     * @param tweetText the body of the tweet's text.
     */
    public LatestTweet(Date createdDate, String tweetText, long tweetId) {
        created = createdDate;
        text = tweetText;
        id = tweetId;
    }

    /**
     * Gets the date the Tweet was created.
     *
     * @return the created date.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the date the Tweet was created.
     *
     * @param created the created date.
     */
    public void setCreated(Date created) {
        this.created = created;
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
     * @param text the text.
     */
    public void setText(String text) {
        this.text = text;
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
     * @param image the image.
     */
    public void setImage(String image) {
        this.image = image;
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
     * @param id the link.
     */
    public void setId(long id) {
        this.id = id;
    }
}
