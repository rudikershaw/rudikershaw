package main.dynamics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.dynamics.entities.Article;
import main.dynamics.repositories.ArticleRepository;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleSessionService articleSessionService;

    @InjectMocks
    private ArticleService service;

    @Test
    public void testGetMostViewedThisWeekReturnsDefaultWhenNoSessions() {
        when(articleSessionService.getMostSessionsThisWeekByArticleId()).thenReturn(null);

        final Article result = service.getMostViewedThisWeek();

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("No Articles Viewed");
    }

    @Test
    public void testGetMostViewedThisWeekReturnsArticleWhenSessionsExist() {
        final Article expected = new Article("Popular", "popular", "img.png", null, "desc");
        expected.setId(1);

        when(articleSessionService.getMostSessionsThisWeekByArticleId()).thenReturn(1);
        when(articleRepository.findById(1)).thenReturn(Optional.of(expected));

        final Article result = service.getMostViewedThisWeek();

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Popular");
    }

    @Test
    public void testInvalidateCacheDoesNotThrow() {
        // invalidateCache is a no-op method; the effect is in the annotations
        service.invalidateCache();
    }
}
