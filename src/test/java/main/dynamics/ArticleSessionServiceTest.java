package main.dynamics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.dynamics.entities.Article;
import main.dynamics.entities.ArticleSession;
import main.dynamics.repositories.ArticleSessionRepository;

@ExtendWith(MockitoExtension.class)
public class ArticleSessionServiceTest {

    @Mock
    private ArticleSessionRepository sessionRepository;

    @Mock
    private RefererService refererService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ArticleSessionService service;

    @Test
    public void testIsUniqueSessionReturnsTrueForNewSession() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("session-123");
        when(sessionRepository.findBySessionIdAndArticleId("session-123", 1)).thenReturn(null);
        when(request.getHeader("User-agent")).thenReturn("Mozilla/5.0");

        final boolean result = service.isUniqueSession(request, article);

        assertThat(result).isTrue();
        verify(sessionRepository).save(any(ArticleSession.class));
        verify(refererService).processReferer(request, article);
    }

    @Test
    public void testIsUniqueSessionReturnsFalseForExistingSession() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);
        final ArticleSession existing = new ArticleSession("session-123", 1);

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("session-123");
        when(sessionRepository.findBySessionIdAndArticleId("session-123", 1)).thenReturn(existing);
        when(request.getHeader("User-agent")).thenReturn("Mozilla/5.0");

        final boolean result = service.isUniqueSession(request, article);

        assertThat(result).isFalse();
    }

    @Test
    public void testIsUniqueSessionReturnsFalseForBot() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);

        when(request.getHeader("User-agent")).thenReturn("Googlebot/2.1");

        final boolean result = service.isUniqueSession(request, article);

        assertThat(result).isFalse();
    }

    @Test
    public void testGetMostSessionsThisWeekByArticleIdReturnsId() {
        when(sessionRepository.findMostVisitedThisWeekArticleIdSince(any())).thenReturn(List.of(1, 2, 3));

        final Integer result = service.getMostSessionsThisWeekByArticleId();

        assertThat(result).isEqualTo(1);
    }

    @Test
    public void testGetMostSessionsThisWeekByArticleIdReturnsNullWhenEmpty() {
        when(sessionRepository.findMostVisitedThisWeekArticleIdSince(any())).thenReturn(Collections.emptyList());

        final Integer result = service.getMostSessionsThisWeekByArticleId();

        assertThat(result).isNull();
    }

    @Test
    public void testCleanOldSessionsNotCalledBelowThreshold() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);

        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("session-123");
        when(sessionRepository.findBySessionIdAndArticleId("session-123", 1)).thenReturn(null);
        when(request.getHeader("User-agent")).thenReturn("Mozilla/5.0");

        service.isUniqueSession(request, article);

        verify(sessionRepository, org.mockito.Mockito.never()).deleteOldSessions(any());
    }
}
