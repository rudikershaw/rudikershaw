package main.dynamics.entities;

import javax.persistence.*;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

/** A user session entity representing a single users visit. */
@Entity
public class ArticleSession {

    private Integer id;
    /** Recording of the Java EE Session Id. */
    private String sessionId;
    /** Recording of the article Id. */
    private Integer articleId;
    /** The date of the session. */
    private Date visited;

    /** No args constructor. */
    public ArticleSession(){
        super();
    }

    public ArticleSession(String sessionId, Integer articleId){
        this.sessionId = sessionId;
        this.articleId = articleId;
        visited = new Date();
    }

    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Date getVisited() {
        return new Date(visited.getTime());
    }

    public void setVisited(Date visited) {
        this.visited = visited == null ? null : new Date(visited.getTime());
    }
}
