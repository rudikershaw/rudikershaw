package main.dynamics.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/** Represents the composite identity of a Referer entity. */
@Embeddable
public class RefererIdentity implements Serializable {

    /** The article a request with a particular referer was for. */
    @ManyToOne
    private Article article;

    /** Referer header of the request for a particular article. */
    private String referer;

    /** Default constructor. */
    public RefererIdentity() {
        super();
    }

    /**
     * Constructor to set the article and referer.
     *
     * @param article the article.
     * @param referer the referer.
     */
    public RefererIdentity(final Article article, final String referer) {
        this.article = article;
        this.referer = referer;
    }

    /**
     * Gets the article to which the referer directed its request.
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
     * Gets the referer.
     *
     * @return the referer.
     */
    public String getReferer() {
        return referer;
    }

    /**
     * Sets the referer.
     *
     * @param refererHeader the referer.
     */
    public void setReferer(final String refererHeader) {
        this.referer = refererHeader;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefererIdentity that = (RefererIdentity) o;
        return Objects.equals(getArticle(), that.getArticle())
                && Objects.equals(getReferer(), that.getReferer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArticle(), getReferer());
    }
}
