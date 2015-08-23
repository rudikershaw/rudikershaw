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
public class HttpBasicsController {

    public static final String PATH = "articles/httpbasics";
    public static final String NAME = "HTTP Basics";
    public static final String IMAGE_PATH = "images/http.jpg";
    public static final String DESCRIPTION = "Having realised that I didn't know anywhere near enough about HTTP, " +
                                             "I put this together as a reference. Have a read.";

    private ArticleService statisticsService;

    @Autowired
    public HttpBasicsController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }
}
