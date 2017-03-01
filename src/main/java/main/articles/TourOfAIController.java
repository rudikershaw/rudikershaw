package main.articles;

import javax.servlet.http.HttpServletRequest;
import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for the Which Search Algorithm? - Part 2 article. */
@Controller
public class TourOfAIController {

    public static final String PATH = "articles/tour-of-ai";
    public static final String NAME = "A Tour of AI";
    public static final String IMAGE_PATH = "images/tour-of-ai.jpg";
    public static final String DESCRIPTION = "It's easy to feel like modern AI is just magic. It isn't, and " +
                                             "this is how it all works.";

    private ArticleService statisticsService;

    @Autowired
    public TourOfAIController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}
