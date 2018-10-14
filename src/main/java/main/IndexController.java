package main;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

import static main.articles.DefaultArticleController.ARTICLES;

/** Controller for the home page. */
@Controller
public class IndexController {

    private static final String PATH = "index";
    public static final int ARTICLES_PER_PAGE = 4;
    private ArticleService articleService;

    @Autowired
    public IndexController(ArticleService articleService){
        this.articleService = articleService;
    }

    /** Mapping for the home page. */
    @RequestMapping("/")
    public String index(Model model){
        List<Article> articles = articles(0);
        model.addAttribute("all", articles);
        model.addAttribute("trending", articleService.getMostViewedThisWeek());
        return PATH;
    }

    /** Get a page (0 based index), by number, of articles to inject into html RESTfully. */
    @ResponseBody
    @RequestMapping("/list/{page}")
    public List<Article> articles(@PathVariable int page) {
        final int from = page < 0 ? 0 : page * ARTICLES_PER_PAGE;
        final int to = from + ARTICLES_PER_PAGE;
        if (from >= 0 && from < ARTICLES.size() && to <= (Integer.MAX_VALUE - ARTICLES_PER_PAGE)) {
            return ARTICLES.subList(from, to > ARTICLES.size() ? ARTICLES.size() : to);
        }
        return Collections.emptyList();
    }
}
