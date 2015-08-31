package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/** Controller for the Defining an Algorithm 3 article. */
@Controller
public class ComputationalMethod3Controller {

    public static final String PATH = "articles/computationalmethod3";
    public static final String NAME = "Defining an Algorithm 3";
    public static final String IMAGE_PATH = "images/taocp.jpg";
    public static final String DESCRIPTION = "The third of a series of posts on interpreting Donald " +
                                             "Knuth's famous volumes; The Art of Computer Programming.";

    private ArticleService statisticsService;

    @Autowired
    public ComputationalMethod3Controller(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }
}
