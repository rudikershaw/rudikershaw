package main.dynamics.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/** Entity representing a unique referer for a particular article. */
@Entity
public class Referer implements Serializable {

    /** The composite ID of the referer. */
    @EmbeddedId
    private RefererIdentity refererIdentity;

    /** The number of times a unique user has requested an article with a particular referer. */
    private long count = 1;

    /** Default constructor. */
    public Referer() {
        super();
    }

    /**
     * Constructor that sets the composite ID of the referer.
     *
     * @param refererIdentity object representing the composite ID of this referer.
     */
    public Referer(final RefererIdentity refererIdentity) {
        this.refererIdentity = refererIdentity;
    }

    /**
     * Gets the referer identity.
     *
     * @return the referer identity.
     */
    public RefererIdentity getRefererIdentity() {
        return refererIdentity;
    }

    /**
     * Sets the referer identity.
     *
     * @param refererIdentity the referer identity.
     */
    public void setRefererIdentity(final RefererIdentity refererIdentity) {
        this.refererIdentity = refererIdentity;
    }

    /**
     * Gets the count.
     *
     * @return the count.
     */
    public long getCount() {
        return count;
    }

    /**
     * Sets the count.
     *
     * @param requestCount the count.
     */
    public void setCount(final long requestCount) {
        this.count = requestCount;
    }
}
