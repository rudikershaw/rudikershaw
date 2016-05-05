package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/** Controller for the Java EE7 Essentials article. */
@Controller
public class JavaEssentialsController {

    public static final String PATH = "articles/java-essentials";
    public static final String NAME = "Event Driven Java EE7";
    public static final String IMAGE_PATH = "images/events.jpg";
    public static final String DESCRIPTION = "After discovering event based annotations I talk through a quick review " +
                                             "of the ways to use event driven programming in Java EE7";

    private ArticleService statisticsService;

    @Autowired
    public JavaEssentialsController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}
