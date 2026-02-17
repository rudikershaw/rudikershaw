package main.reddit;

import java.util.Date;

/** POJO for the salient Reddit post details needed in the view layer. */
public class LatestRedditPost {

    /** The title of the post. */
    private String title;

    /** The selftext/body of the post. */
    private String selftext;

    /** The subreddit the post was made in. */
    private String subreddit;

    /** The author's username. */
    private String author;

    /** The permalink to the post. */
    private String permalink;

    /** The score (upvotes - downvotes). */
    private int score;

    /** The number of comments. */
    private int numComments;

    /** The date the post was created. */
    private Date created;

    /** The thumbnail URL if available. */
    private String thumbnail;

    /** Default constructor. */
    public LatestRedditPost() {
        super();
    }

    /**
     * Gets the title.
     *
     * @return the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param postTitle the title.
     */
    public void setTitle(final String postTitle) {
        this.title = postTitle;
    }

    /**
     * Gets the selftext.
     *
     * @return the selftext.
     */
    public String getSelftext() {
        return selftext;
    }

    /**
     * Sets the selftext.
     *
     * @param postSelftext the selftext.
     */
    public void setSelftext(final String postSelftext) {
        this.selftext = postSelftext;
    }

    /**
     * Gets the subreddit.
     *
     * @return the subreddit.
     */
    public String getSubreddit() {
        return subreddit;
    }

    /**
     * Sets the subreddit.
     *
     * @param postSubreddit the subreddit.
     */
    public void setSubreddit(final String postSubreddit) {
        this.subreddit = postSubreddit;
    }

    /**
     * Gets the author.
     *
     * @return the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author.
     *
     * @param postAuthor the author.
     */
    public void setAuthor(final String postAuthor) {
        this.author = postAuthor;
    }

    /**
     * Gets the permalink.
     *
     * @return the permalink.
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * Sets the permalink.
     *
     * @param postPermalink the permalink.
     */
    public void setPermalink(final String postPermalink) {
        this.permalink = postPermalink;
    }

    /**
     * Gets the score.
     *
     * @return the score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score.
     *
     * @param postScore the score.
     */
    public void setScore(final int postScore) {
        this.score = postScore;
    }

    /**
     * Gets the number of comments.
     *
     * @return the numComments.
     */
    public int getNumComments() {
        return numComments;
    }

    /**
     * Sets the number of comments.
     *
     * @param comments the numComments.
     */
    public void setNumComments(final int comments) {
        this.numComments = comments;
    }

    /**
     * Gets the created date.
     *
     * @return the created date.
     */
    public Date getCreated() {
        return created == null ? null : new Date(created.getTime());
    }

    /**
     * Sets the created date.
     *
     * @param postCreated the created date.
     */
    public void setCreated(final Date postCreated) {
        this.created = postCreated == null ? null : new Date(postCreated.getTime());
    }

    /**
     * Gets the thumbnail.
     *
     * @return the thumbnail.
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the thumbnail.
     *
     * @param postThumbnail the thumbnail.
     */
    public void setThumbnail(final String postThumbnail) {
        this.thumbnail = postThumbnail;
    }
}
