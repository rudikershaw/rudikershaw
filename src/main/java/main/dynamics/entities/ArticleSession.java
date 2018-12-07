package main.dynamics.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

/** A user session entity representing a single users visit. */
@Entity
public class ArticleSession {

    /** The entity ID. */
    private Integer id;

    /** Recording of the Java EE Session Id. */
    private String sessionId;

    /** Recording of the article Id. */
    private Integer articleId;

    /** The date of the session. */
    private Date visited;

    /** No args constructor. */
    public ArticleSession() {
        super();
    }

    /**
     * Constructor.
     * @param session the unique session ID (JSESSION).
     * @param article the entity ID for the article that the user viewed.
     */
    public ArticleSession(final String session, final Integer article) {
        this.sessionId = session;
        this.articleId = article;
        visited = new Date();
    }

    /**
     * The ID of this entity.
     * @return the entity ID.
     */
    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer getId() {
        return id;
    }

    /**
     * Set the ID for this entity.
     * @param identifier the ID to set.
     */
    public void setId(final Integer identifier) {
        this.id = identifier;
    }

    /**
     * Get the session ID.
     * @return the session ID.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Set the session ID.
     * @param session the session ID to set.
     */
    public void setSessionId(final String session) {
        this.sessionId = session;
    }

    /**
     * Get the article ID.
     * @return the article ID.
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * Set the article ID.
     * @param article the ID of the article.
     */
    public void setArticleId(final Integer article) {
        this.articleId = article;
    }

    /**
     * Get the date of the visit to the article.
     * @return the visited date.
     */
    public Date getVisited() {
        return new Date(visited.getTime());
    }

    /**
     * Set the date of this visit.
     * @param visitedDate the date of this visit.
     */
    public void setVisited(final Date visitedDate) {
        this.visited = visitedDate == null ? null : new Date(visitedDate.getTime());
    }
}
