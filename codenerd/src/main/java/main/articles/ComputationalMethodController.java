package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class ComputationalMethodController {

    public static final String PATH = "articles/computationalmethod";
    public static final String NAME = "Defining an Algorithm";
    public static final String IMAGE_PATH = "images/taocp.jpg";
    public static final String DESCRIPTION = "The first of a series of posts on interpreting Donald " +
                                             "Knuth's famous volumes; The Art of Computer Programming.";

    private ArticleService statisticsService;

    @Autowired
    public ComputationalMethodController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }
}
