package main.dynamics;

import main.dynamics.entities.Article;
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

    /** Injected article session respository. */
    private final ArticleSessionRepository sessionRepository;

    /** Injected referrer service. */
    private final ReferrerService referrerService;

    /** Counter of how many times articles have been viewed but expired sessions have not been cleared. */
    private int counter = 0;

    /** 2 days in milliseconds. */
    private static final long TWO_DAY_MS = 172800000;

    /** Number of times articles can be viewed before we should clean expired sessions from the DB. */
    private static final int ATTEMPTS_BEFORE_CLEAN = 100;

    /**
     * Constructor for dependency injection.
     *
     * @param sessionRepository injected article session repository.
     * @param referrerService injected referrer service.
     */
    @Autowired
    public ArticleSessionService(final ArticleSessionRepository sessionRepository, final ReferrerService referrerService) {
        this.sessionRepository = sessionRepository;
        this.referrerService = referrerService;
    }

    /**
     * Checks whether this is the first view of this article by the given session.
     *
     * @param request the request to get the session from.
     * @param article the article to check whether it is their first view of.
     * @return whether this was the first view of the article.
     */
    public boolean isUniqueSession(final HttpServletRequest request, final Article article) {
        cleanOldSessions();
        if (isBot(request)) {
            return false;
        }

        final String sessionId = request.getSession().getId();
        final ArticleSession userSession = sessionRepository.findBySessionIdAndArticleId(sessionId, article.getId());
        if (userSession == null) {
            sessionRepository.save(new ArticleSession(request.getSession().getId(), article.getId()));
            referrerService.processReferrer(request, article);
            return true;
        }
        return false;
    }

    /**
     * Find the article most visited by unique sessions.
     *
     * @return the ID pf the article most viewed.
     */
    public Integer getMostSessionsThisWeekByArticleId() {
        final List<Integer> articleIds = sessionRepository.findMostVisitedThisWeekArticleId();
        if (articleIds.isEmpty()) {
            return null;
        } else {
            return articleIds.get(0);
        }
    }

    /** Clean up old sessions after a while. */
    private void cleanOldSessions() {
        counter++;
        if (counter >= ATTEMPTS_BEFORE_CLEAN) {
            sessionRepository.deleteOldSessions(new Date(new Date().getTime() - TWO_DAY_MS));
        }
        counter %= ATTEMPTS_BEFORE_CLEAN;
    }

    /**
     * Checks whether the requests is likely to be from a bot.
     *
     * @param request the request to check.
     * @return whether the request is likely to be from a bot.
     */
    private boolean isBot(final HttpServletRequest request) {
        final String userAgent = request.getHeader("User-agent").toLowerCase();
        return userAgent.contains("bot") || userAgent.contains("crawler");
    }
}
