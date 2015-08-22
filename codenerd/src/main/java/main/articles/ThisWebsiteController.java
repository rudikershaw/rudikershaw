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
public class ThisWebsiteController {

    public static final String PATH = "articles/website-upgrade";
    public static final String NAME = "Upgrading This Website";
    public static final String IMAGE_PATH = "images/websiteupgrade.jpg";
    public static final String DESCRIPTION = "After months of inactivity I have upgraded this website to use JavaEE & " +
                                             "Spring boot, as well as a MySQL database for some fun extras.";

    private ArticleService statisticsService;

    @Autowired
    public ThisWebsiteController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }
}
