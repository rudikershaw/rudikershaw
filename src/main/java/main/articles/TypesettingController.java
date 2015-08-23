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
public class TypesettingController {

    public static final String PATH = "articles/typesetting";
    public static final String NAME = "Taking Typesetting Seriously";
    public static final String IMAGE_PATH = "images/typesetting.jpg";
    public static final String DESCRIPTION = "In 'Taking Typesetting Seriously' I talk about the @font-face " +
                                             "css and how you can declare custom fonts for your website.";

    private ArticleService statisticsService;

    @Autowired
    public TypesettingController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }
}
