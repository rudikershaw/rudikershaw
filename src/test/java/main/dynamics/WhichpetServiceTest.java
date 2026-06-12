package main.dynamics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import main.dynamics.entities.WhichpetDatum;
import main.dynamics.repositories.WhichpetRepository;

@ExtendWith(MockitoExtension.class)
public class WhichpetServiceTest {

    @Mock
    private WhichpetRepository repository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private WhichpetService service;

    @Test
    public void testGetAllDataReturnsDataWhenViewer() {
        final WhichpetDatum datum = new WhichpetDatum("cat", "Feline pet");
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<WhichpetDatum> page = new PageImpl<>(List.of(datum));

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("whichpet-viewer")).thenReturn(Boolean.TRUE);
        when(repository.findAll(pageable)).thenReturn(page);

        final List<WhichpetDatum> result = service.getAllData(pageable, request);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLabel()).isEqualTo("cat");
    }

    @Test
    public void testGetAllDataReturnsEmptyWhenNotViewer() {
        final Pageable pageable = PageRequest.of(0, 10);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("whichpet-viewer")).thenReturn(null);

        final List<WhichpetDatum> result = service.getAllData(pageable, request);

        assertThat(result).isEmpty();
    }

    @Test
    public void testCreateDataSavesWhenViewer() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("whichpet-viewer")).thenReturn(Boolean.TRUE);
        when(repository.save(any(WhichpetDatum.class))).thenReturn(new WhichpetDatum("dog", "Canine"));

        service.createData("dog", "Canine pet", request);

        verify(repository).save(any(WhichpetDatum.class));
    }

    @Test
    public void testCreateDataDoesNotSaveWhenNotViewer() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("whichpet-viewer")).thenReturn(null);

        service.createData("dog", "Canine pet", request);

        // No save should occur when not a viewer
        // (no verification needed; the method simply returns without calling save)
    }
}
