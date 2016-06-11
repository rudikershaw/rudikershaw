package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.WhichpetService;
import main.dynamics.entities.Article;
import main.dynamics.entities.WhichpetDatum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/** Controller for the Machine Learning Basics article. */
@Controller
public class WhichpetController {

    public static final String PATH = "articles/whichpet";
    public static final String WHICHPET_DATA_PATH = "fragments/whichpet";
    public static final String WHICHPET_ADD_PATH = "fragments/whichpet/add";
    public static final String NAME = "Whichpet";
    public static final String IMAGE_PATH = "images/whichpet-orange.png";
    public static final String DESCRIPTION = "Whichpet is a Javascript library for classifying text descriptions into " +
                                             "labels. In this case pet descriptions and the type of pets.";

    private ArticleService statisticsService;
    private WhichpetService whichpetService;

    @Autowired
    public WhichpetController(ArticleService statisticsService, WhichpetService whichpetService){
        this.statisticsService = statisticsService;
        this.whichpetService = whichpetService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        request.getSession().setAttribute("whichpet-viewer", Boolean.TRUE);
        return PATH;
    }

    @RequestMapping(WHICHPET_DATA_PATH)
    public String getWhichpetData(Model model, HttpServletRequest request){
        List<WhichpetDatum> pets = whichpetService.getAllData(new PageRequest(0, 100000), request);
        WhichpetDatum[] array = pets.toArray(new WhichpetDatum[pets.size()]);
        model.addAttribute("pets", array);
        return WHICHPET_DATA_PATH;
    }

    @ResponseBody
    @RequestMapping(WHICHPET_ADD_PATH)
    public String addWhichpetData(HttpServletRequest request, @RequestParam(required = true, value = "label") String label,
                                  @RequestParam(required = true, value = "description") String description){
        String referer = request.getHeader("referer");
        if(referer != null && referer.contains(PATH)){
            whichpetService.createData(label, description, request);
        }
        return "Success";
    }
}
