package main.articles;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import main.dynamics.WhichpetService;
import main.dynamics.entities.WhichpetDatum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/** Controller for the Machine Learning Basics article. */
@Controller
public class WhichpetController {

    private static final String PATH = "articles/whichpet";
    private static final String WHICHPET_DATA_PATH = "fragments/whichpet";
    private static final String WHICHPET_ADD_PATH = "fragments/whichpet/add";

    private WhichpetService whichpetService;
    private DefaultArticleController articleController;

    @Autowired
    public WhichpetController(WhichpetService whichpetService, DefaultArticleController articleController) {
        this.whichpetService = whichpetService;
        this.articleController = articleController;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request) {
        request.getSession().setAttribute("whichpet-viewer", Boolean.TRUE);
        return articleController.article("whichpet", model, request);
    }

    @RequestMapping(WHICHPET_DATA_PATH)
    public String getWhichpetData(Model model, HttpServletRequest request) {
        List<WhichpetDatum> pets = whichpetService.getAllData(new PageRequest(0, 100000), request);
        WhichpetDatum[] array = pets.toArray(new WhichpetDatum[pets.size()]);
        model.addAttribute("pets", array);
        return WHICHPET_DATA_PATH;
    }

    @ResponseBody
    @RequestMapping(WHICHPET_ADD_PATH)
    public String addWhichpetData(HttpServletRequest request, @RequestParam(value = "label") String label,
                                  @RequestParam(value = "description") String description) {
        String referer = request.getHeader("referer");
        if(referer != null && referer.contains(PATH)) {
            whichpetService.createData(label, description, request);
        }
        return "Success";
    }
}
