package main.dynamics.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import main.dynamics.entities.ArticleSession;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleSessionRepositoryTest {

    @Autowired
    private ArticleSessionRepository sessionRepository;

    @Test
    public void testArticleSessionCrud() {
        final ArticleSession session = createSession();
        final String id = session.getSessionId();
        final int article = session.getArticleId();
        sessionRepository.save(session);

        final ArticleSession loadedSession = sessionRepository.findBySessionIdAndArticleId(id, article);

        assertThat(loadedSession).isEqualTo(session);
    }

    @Test
    public void testDeleteSessionsBefore()
    {
        final ArticleSession session = createSession();
        sessionRepository.save(session);

        sessionRepository.deleteOldSessions(new Date(System.currentTimeMillis() + 1000));

        assertThat(sessionRepository.findAll()).isEmpty();
    }

    public ArticleSession createSession()
    {
        return new ArticleSession(UUID.randomUUID().toString(), 1);
    }
}
