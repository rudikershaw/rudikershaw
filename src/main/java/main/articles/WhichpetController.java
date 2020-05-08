package main.articles;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.dynamics.WhichpetService;
import main.dynamics.entities.WhichpetDatum;

/** Controller for the Machine Learning Basics article. */
@Controller
class WhichpetController {

    /** The mapping for the Whichpet view template. */
    private static final String PATH = "articles/whichpet";

    /** Mapping and path for the data fragment template. */
    private static final String WHICHPET_DATA_PATH = "fragments/whichpet";

    /** Mapping for the whichpet add end-point. */
    private static final String WHICHPET_ADD_PATH = "fragments/whichpet/add";

    /** Injected Whichpet service. */
    private final WhichpetService whichpetService;

    /** Injected default article controller to delegate page views to. */
    private final DefaultArticleController articleController;

    /**
     * Constructor for dependency injection.
     *
     * @param injectedWhichpetService injected whichpet service.
     * @param injectedArticleController injected article controller.
     */
    @Autowired
    WhichpetController(final WhichpetService injectedWhichpetService, final DefaultArticleController injectedArticleController) {
        this.whichpetService = injectedWhichpetService;
        this.articleController = injectedArticleController;
    }

    /**
     * Mapping for the whichpet example application article.
     *
     * @param model injected model.
     * @param request injected request.
     * @return the path to the view template.
     */
    @GetMapping(PATH)
    public String article(final Model model, final HttpServletRequest request) {
        request.getSession().setAttribute("whichpet-viewer", Boolean.TRUE);
        return articleController.article("whichpet", model, request);
    }

    /**
     * Mapping for the whichpet data fragment.
     *
     * @param model injected model.
     * @param request injected request.
     * @return the path to the fragment template.
     */
    @GetMapping(WHICHPET_DATA_PATH)
    public String getWhichpetData(final Model model, final HttpServletRequest request) {
        final List<WhichpetDatum> pets = whichpetService.getAllData(PageRequest.of(0, 100000), request);
        final WhichpetDatum[] array = pets.toArray(new WhichpetDatum[0]);
        model.addAttribute("pets", array);
        return WHICHPET_DATA_PATH;
    }

    /**
     * Mapping for end-point to add data for the whichpet example app to use.
     *
     * @param request injected request.
     * @param label label to add a description for.
     * @param description the description.
     * @return success.
     */
    @ResponseBody
    @GetMapping(WHICHPET_ADD_PATH)
    public String addWhichpetData(final HttpServletRequest request, @RequestParam(value = "label") final String label,
                                  @RequestParam(value = "description") final String description) {
        final String referer = request.getHeader("referer");
        if (referer != null && referer.contains(PATH)) {
            whichpetService.createData(label, description, request);
        }
        return "Success";
    }
}
