package main.dynamics;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import main.dynamics.entities.WhichpetDatum;
import main.dynamics.repositories.WhichpetRepository;

/** Service for handling Whichpet data for the example on the website. */
@Service
public class WhichpetService {

    /** Injected Whichpet repository. */
    private final WhichpetRepository repository;

    /**
     * Constructor for dependency injection.
     *
     * @param whichpetRepository injected Whichpet repository.
     */
    @Autowired
    public WhichpetService(final WhichpetRepository whichpetRepository) {
       this.repository = whichpetRepository;
    }

    /**
     * Get all label description pairs for populating Whichpet classifier.
     *
     * @param pageable pageable.
     * @param request the request for validation.
     * @return a list of entries.
     */
    public List<WhichpetDatum> getAllData(final Pageable pageable, final HttpServletRequest request) {
        final Object viewer = request.getSession().getAttribute("whichpet-viewer");
        if (viewer instanceof Boolean && (Boolean) viewer) {
            final Page<WhichpetDatum> data = repository.findAll(pageable);
            return data != null && data.getContent() != null ? data.getContent() : new ArrayList<>();
        }
        return new ArrayList<>();
    }

    /**
     * Takes a label and description and the request and created a datum entry for retrieval later.
     *
     * @param label the text label.
     * @param description the description under that label.
     * @param request the request for validation.
     */
    public void createData(final String label, final String description, final HttpServletRequest request) {
        final Object viewer = request.getSession().getAttribute("whichpet-viewer");
        if (viewer instanceof Boolean && (Boolean) viewer) {
            repository.save(new WhichpetDatum(label, description));
        }
    }
}
