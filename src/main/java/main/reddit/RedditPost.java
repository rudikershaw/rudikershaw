package main.reddit;

/** A plain data object representing a Reddit post for JSON serialisation. */
public class RedditPost {

    /** The subreddit name, without the leading "r/", or null. */
    private final String subreddit;

    /** The published date formatted as "MMM dd", or null. */
    private final String published;

    /** The post title. */
    private final String title;

    /** The plain text post description, or null. */
    private final String description;

    /** The link to the post on Reddit. */
    private final String link;

    /** The thumbnail image URL, or null. */
    private final String thumbnail;

    /**
     * Construct a Reddit post DTO.
     *
     * @param postSubreddit the subreddit name.
     * @param postPublished the formatted published date.
     * @param postTitle the post title.
     * @param postDescription the plain text description.
     * @param postLink the link to the post.
     * @param postThumbnail the thumbnail image URL, or null.
     */
    public RedditPost(final String postSubreddit, final String postPublished,
                      final String postTitle, final String postDescription,
                      final String postLink, final String postThumbnail) {
        this.subreddit = postSubreddit;
        this.published = postPublished;
        this.title = postTitle;
        this.description = postDescription;
        this.link = postLink;
        this.thumbnail = postThumbnail;
    }

    /** @return the subreddit name, or null. */
    public String getSubreddit() {
        return subreddit;
    }

    /** @return the formatted published date, or null. */
    public String getPublished() {
        return published;
    }

    /** @return the post title. */
    public String getTitle() {
        return title;
    }

    /** @return the plain text description, or null. */
    public String getDescription() {
        return description;
    }

    /** @return the link to the post. */
    public String getLink() {
        return link;
    }

    /** @return the thumbnail image URL, or null. */
    public String getThumbnail() {
        return thumbnail;
    }
}
