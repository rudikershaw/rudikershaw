package main;

import main.dynamics.ArticleService;
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
    ArticleService statisticsService;

    @Autowired
    public IndexController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        List<Article> articles = statisticsService.getAllArticles();
        model.addAttribute("all", articles);
        model.addAttribute("mostRecent", getMostRecent(articles));
        model.addAttribute("mostViewed", getMostViewed(articles));
        return PATH;
    }

    /** Get the most recently published article from a list of articles. */
    private Article getMostRecent(List<Article> articles){
        Article mostRecent = null;
        for(Article article : articles){
            if(mostRecent == null || article.getPublished().before(mostRecent.getPublished())){
                mostRecent = article;
            }
        }
        return mostRecent;
    }

    /** Get the most viewed article from a list of articles. */
    private Article getMostViewed(List<Article> articles){
        Article mostViewed = null;
        for(Article article : articles){
            if(mostViewed == null || article.getViews() > mostViewed.getViews()){
                mostViewed = article;
            }
        }
        return mostViewed;
    }
}
