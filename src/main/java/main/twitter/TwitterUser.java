package main.twitter;

/** POJO representing a twitter user. */
public class TwitterUser {

    /** The base 64 string value of the mini profile image. */
    private String image;

    /** The human readable name of the user. */
    private String name;

    /** The username of the user. */
    private String screenName;

    /**
     * Gets the base 64 string value of the mini profile image.
     *
     * @return the image.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the base 64 string value of the mini profile image.
     *
     * @param tweetImage the image.
     */
    public void setImage(final String tweetImage) {
        this.image = tweetImage;
    }

    /**
     * Gets the human readable disaply name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the human readable disaply name.
     *
     * @param displayName the name.
     */
    public void setName(final String displayName) {
        this.name = displayName;
    }

    /**
     * Gets the screenName.
     *
     * @return the screenName.
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username.
     */
    public void setScreenName(final String username) {
        this.screenName = username;
    }
}
