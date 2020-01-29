package main.dynamics.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/** Entity representing a unique referrer for a particular article. */
@Entity
public class Referrer implements Serializable {

    /** The composite ID of the referrer. */
    @EmbeddedId
    private ReferrerIdentity referrerIdentity;

    /** The number of times a unique user has requested an article with a particular referrer. */
    private long count;

    /** Default constructor. */
    public Referrer() {
        super();
    }

    /**
     * Constructor that sets the composite ID of the referrer.
     *
     * @param referrerIdentity object representing the composite ID of this referrer.
     */
    public Referrer(final ReferrerIdentity referrerIdentity) {
        this.referrerIdentity = referrerIdentity;
    }

    /**
     * Gets the referrer identity.
     *
     * @return the referrer identity.
     */
    public ReferrerIdentity getReferrerIdentity() {
        return referrerIdentity;
    }

    /**
     * Sets the referrer identity.
     *
     * @param referrerIdentity the referrer identity.
     */
    public void setReferrerIdentity(final ReferrerIdentity referrerIdentity) {
        this.referrerIdentity = referrerIdentity;
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
