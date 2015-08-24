package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Rudi Kershaw on 23/08/2015.
 */
@Controller
public class PhpAdventureController {

    public static final String PATH = "articles/php-adventure";
    public static final String NAME = "A PHP Adventure";
    public static final String IMAGE_PATH = "images/game.jpg";
    public static final String DESCRIPTION = "An ironic text based adventure game written in PHP about a " +
                                             "developer who writes an ironic text based adventure game.";

    private ArticleService statisticsService;

    @Autowired
    public PhpAdventureController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return "redirect:http://rudikershaw.co.nf/subsites/phpadventure/begin.php";
    }
}
