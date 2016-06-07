package main.dynamics;

import main.dynamics.entities.ArticleSession;
import main.dynamics.repositories.ArticleSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/** Service for checking whether views are unique by session. */
@Service
public class ArticleSessionService {

    private ArticleSessionRepository sessionRepository;
    private int counter = 0;
    private static final long TWO_DAY_MS = 1000 * 60 * 60 * 24 * 2;
    private static final int ATTEMPTS_BEFORE_CLEAN = 100;

    @Autowired
    public ArticleSessionService(ArticleSessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    public boolean isUniqueSession(HttpServletRequest request, Integer articleId){
        cleanOldSessions();
        if(isBot(request)) return false;
        ArticleSession userSession = sessionRepository.findBySessionIdAndArticleId(request.getSession().getId(), articleId);
        if(userSession == null){
            sessionRepository.save(new ArticleSession(request.getSession().getId(), articleId));
            return true;
        }
        return false;
    }

    public Integer getMostSessionsThisWeekByArticleId(){
        List<Integer> articleIds = sessionRepository.findMostVisitedThisWeekArticleId();
        if(articleIds.isEmpty()) return null;
        else return articleIds.get(0);
    }

    private void cleanOldSessions(){
        counter++;
        if(counter >= ATTEMPTS_BEFORE_CLEAN){
            sessionRepository.deleteOldSessions(new Date(new Date().getTime() - TWO_DAY_MS));
        }
        counter %= ATTEMPTS_BEFORE_CLEAN;
    }

    private boolean isBot(HttpServletRequest request)
    {
        String userAgent = request.getHeader("User-agent").toLowerCase();
        if(userAgent.contains("bot") || userAgent.contains("crawler"))
        {
            return true;
        }
        return false;
    }
}
