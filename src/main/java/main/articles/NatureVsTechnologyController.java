package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/** Controller for the Which Search Algorithm? - Part 2 article. */
@Controller
public class NatureVsTechnologyController {

    public static final String PATH = "articles/nature-vs-technology";
    public static final String NAME = "Nature vs Technology";
    public static final String IMAGE_PATH = "images/nature.jpg";
    public static final String DESCRIPTION = "Is nature at odds with technology? The ramblings of a " +
                                             "science enthusiast gone camping.";

    private ArticleService statisticsService;

    @Autowired
    public NatureVsTechnologyController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}
