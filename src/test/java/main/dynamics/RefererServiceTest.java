package main.dynamics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.dynamics.entities.Article;
import main.dynamics.entities.Referer;
import main.dynamics.entities.RefererIdentity;
import main.dynamics.repositories.RefererRepository;

@ExtendWith(MockitoExtension.class)
public class RefererServiceTest {

    @Mock
    private RefererRepository repository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RefererService service;

    @Test
    public void testListByArticleIdReturnsReferers() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);
        final Referer referer = new Referer(new RefererIdentity(article, "https://google.com"));

        when(repository.findAllByRefererIdentityArticleId(1)).thenReturn(List.of(referer));

        final List<Referer> result = service.listByArticleId(1);

        assertThat(result).hasSize(1);
    }

    @Test
    public void testProcessRefererCreatesNewReferer() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);

        when(request.getHeader("Referer")).thenReturn("https://google.com");
        when(repository.findByRefererIdentityRefererAndRefererIdentityArticleId("https://google.com", 1)).thenReturn(null);
        when(repository.save(any(Referer.class))).thenReturn(new Referer());

        service.processReferer(request, article);

        verify(repository).save(any(Referer.class));
    }

    @Test
    public void testProcessRefererIncrementsExistingReferer() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);
        final Referer existing = new Referer(new RefererIdentity(article, "https://google.com"));
        existing.setCount(5);

        when(request.getHeader("Referer")).thenReturn("https://google.com");
        when(repository.findByRefererIdentityRefererAndRefererIdentityArticleId("https://google.com", 1)).thenReturn(existing);

        service.processReferer(request, article);

        assertThat(existing.getCount()).isEqualTo(6);
        verify(repository).save(existing);
    }

    @Test
    public void testProcessRefererDoesNothingWhenRefererBlank() {
        final Article article = new Article("Test", "test", "img.png", null, "desc");
        article.setId(1);

        when(request.getHeader("Referer")).thenReturn(null);

        service.processReferer(request, article);

        verify(repository, org.mockito.Mockito.never()).save(any(Referer.class));
    }
}
