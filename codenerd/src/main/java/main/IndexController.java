package main;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/** Controller for the home page. */
@Controller
public class IndexController {

    public static final String PATH = "index";
    public static final int ARTICLES_PER_PAGE = 4;
    ArticleService articleService;

    @Autowired
    public IndexController(ArticleService articleService){
        this.articleService = articleService;
    }

    @RequestMapping("/")
    public String index(Model model){
        List<Article> articles = articles(0);
        model.addAttribute("all", articles);
        model.addAttribute("mostRecent", getMostRecent(articles));
        model.addAttribute("mostViewed", getMostViewed(articles));
        return PATH;
    }

    /** Get a page (0 based index), by number, of articles to inject into html RESTfully. */
    @ResponseBody
    @RequestMapping("/{page}")
    public List<Article> articles(@PathVariable int page){
        int start = page * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE;
        Pageable pageable = new PageRequest(start, end);
        return articleService.getPageOfArticles(pageable);
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
