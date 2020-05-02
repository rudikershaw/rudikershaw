package main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for the bibliography page. */
@Controller
public class BibliographyController {

    /** The path to the bibliography page's view template. */
    private static final String PATH = "bibliography";

    /**
     * Mapping for the bibliography page.
     *
     * @return the path to the view template.
     */
    @RequestMapping("/bibliography")
    public String index() {
        return PATH;
    }
}
