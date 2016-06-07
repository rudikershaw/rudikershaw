package main.dynamics;

import main.dynamics.entities.WhichpetDatum;
import main.dynamics.repositories.WhichpetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rudi Kershaw on 06/07/2016.
 */
@Service
public class WhichpetService {

    WhichpetRepository repository;

    @Autowired
    public WhichpetService(WhichpetRepository repository){
       this.repository = repository;
    }

    public List<WhichpetDatum> getAllData(Pageable pageable, HttpServletRequest request){
        final Object viewer = request.getSession().getAttribute("whichpet-viewer");
        if (viewer instanceof Boolean && (Boolean) viewer){
            Page<WhichpetDatum> data = repository.findAll(pageable);
            return data != null && data.getContent() != null ? data.getContent() : new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public void createData(String label, String description, HttpServletRequest request){
        final Object viewer = request.getSession().getAttribute("whichpet-viewer");
        if (viewer instanceof Boolean && (Boolean) viewer){
            repository.save(new WhichpetDatum(label, description));
        }
    }
}
