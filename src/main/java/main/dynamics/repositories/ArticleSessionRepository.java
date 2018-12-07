package main.dynamics.repositories;

import main.dynamics.entities.ArticleSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/** Spring CRUD repository for ArticleSessions entities. */
@Transactional
public interface ArticleSessionRepository extends CrudRepository<ArticleSession, Integer> {

    /**
     * Find all ArticleSessions.
     * @return list of all ArticleSession entities.
     */
    List<ArticleSession> findAll();

    /**
     * Find by session ID and article ID.
     * @param sessionId the session ID.
     * @param articleId the article ID.
     * @return an ArticleSession or null.
     */
    ArticleSession findBySessionIdAndArticleId(String sessionId, Integer articleId);

    /**
     * Delete all sessions from before a given date.
     * @param date the date before which to delete old sessions.
     * @return the number of deleted ArticleSession entities.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ArticleSession WHERE visited < ?#{[0]}")
    Integer deleteOldSessions(Date date);

    /**
     * Find the IDs of articles viewed this week ordered by most visited first.
     * @return a list of article IDs ordered by most visited first.
     */
    @Query("SELECT articleId FROM ArticleSession "
            + "WHERE visited > subdate(current_date, 7) GROUP BY articleId "
            + "ORDER BY COUNT(*) DESC")
    List<Integer> findMostVisitedThisWeekArticleId();
}
