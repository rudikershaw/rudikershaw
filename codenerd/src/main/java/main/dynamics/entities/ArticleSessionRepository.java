package main.dynamics.entities;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Rudi Kershaw on 16/08/2015.
 */
@Transactional
public interface ArticleSessionRepository extends CrudRepository<ArticleSession, Integer> {

    List<ArticleSession> findAll();

    ArticleSession findBySessionIdAndArticleId(String sessionId, Integer articleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ArticleSession WHERE visited < ?#{[0]}")
    Integer deleteOldSessions(Date date);
}
