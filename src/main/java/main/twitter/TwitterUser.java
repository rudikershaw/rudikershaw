package main.twitter;

/** POJO representing a twitter user. */
public class TwitterUser {

    /** The base 64 string value of the mini profile image. */
    private String image;

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
}
