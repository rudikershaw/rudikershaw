package main.dynamics.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/** Represents the composite identity of a Referrer entity. */
@Embeddable
public class ReferrerIdentity implements Serializable {

    /** The article a request with a particular referrer was for. */
    @ManyToOne
    private Article article;

    /** Referrer header of the request for a particular article. */
    private String referrer;

    /** Default constructor. */
    public ReferrerIdentity() {
        super();
    }

    /**
     * Constructor to set the article and referrer.
     *
     * @param article the article.
     * @param referrer the referrer.
     */
    public ReferrerIdentity(final Article article, final String referrer) {
        this.article = article;
        this.referrer = referrer;
    }

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReferrerIdentity that = (ReferrerIdentity) o;
        return Objects.equals(getArticle(), that.getArticle())
                && Objects.equals(getReferrer(), that.getReferrer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArticle(), getReferrer());
    }
}
