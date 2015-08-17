package main.dynamics;

import main.dynamics.entities.ArticleSession;
import main.dynamics.entities.ArticleSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

/** Service for checking whether views are unique by session. */
@Service
public class ArticleSessionService {

    ArticleSessionRepository sessionRepository;
    private int counter = 0;
    private static final long TWO_DAY_MS = 1000 * 60 * 60 * 24 * 2;

    @Autowired
    public ArticleSessionService(ArticleSessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    public boolean isUniqueSession(HttpSession session, Integer articleId){
        deleteOldSessions();
        ArticleSession userSession = sessionRepository.findBySessionIdAndArticleId(session.getId(), articleId);
        if(userSession == null){
            sessionRepository.save(new ArticleSession(session.getId(), articleId));
            return true;
        }
        return false;
    }

    private void deleteOldSessions(){
        counter++;
        if(counter >= 10){
            sessionRepository.deleteOldSessions(new Date(new Date().getTime() - TWO_DAY_MS));
        }
        counter %= 10;
    }
}
