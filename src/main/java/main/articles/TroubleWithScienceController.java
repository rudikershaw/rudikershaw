package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/** Controller for the Trouble with Science article. */
@Controller
public class TroubleWithScienceController {

    public static final String PATH = "articles/science-trouble";
    public static final String NAME = "The Trouble with Science";
    public static final String IMAGE_PATH = "images/science-trouble.jpg";
    public static final String DESCRIPTION = "Science is how we know what is true and what is not. This article covers " +
                                             "the biggest problems that science faces right now.";

    private ArticleService statisticsService;

    @Autowired
    public TroubleWithScienceController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}
