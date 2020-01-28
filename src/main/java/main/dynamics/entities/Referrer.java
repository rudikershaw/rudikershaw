package main.dynamics.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/** Entity representing a unique referrer for a particular article. */
@Entity
public class Referrer implements Serializable {

    /** The article a request with a particular referrer was for. */
    @Id
    @ManyToOne
    private Article article;

    /** Referrer header of the request for a particular article. */
    @Id
    private String referrer;

    /** The number of times a unique user has requested an article with a particular referrer. */
    private long count;

    /**
     * Gets the article to which the referrer directed its request.
     *
     * @return the article.
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Sets the article.
     *
     * @param articleReferredTo the article.
     */
    public void setArticle(final Article articleReferredTo) {
        this.article = articleReferredTo;
    }

    /**
     * Gets the referrer.
     *
     * @return the referrer.
     */
    public String getReferrer() {
        return referrer;
    }

    /**
     * Sets the referrer.
     *
     * @param referrerHeader the referrer.
     */
    public void setReferrer(final String referrerHeader) {
        this.referrer = referrerHeader;
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
