package main;

import main.dynamics.ArticleStatisticsService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/** Controller for the home page. */
@Controller
public class IndexController {

    public static final String PATH = "index";
    ArticleStatisticsService statisticsService;

    @Autowired
    public IndexController(ArticleStatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        List<Article> articles = statisticsService.getAllArticles();
        return PATH;
    }
}
