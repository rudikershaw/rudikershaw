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

    /** The path to the index page's view template. */
    private static final String PATH = "index";

    /** Number of articles in a page as listed by the list end-point. */
    static final int ARTICLES_PER_PAGE = 4;

    /** Injected article service. */
    private final ArticleService articleService;

    /**
     * Constructor used to dependency injection.
     *
     * @param injectedArticleService article service to be injected.
     */
    @Autowired
    public IndexController(final ArticleService injectedArticleService) {
        this.articleService = injectedArticleService;
    }

    /**
     * Mapping for the home page.
     *
     * @param model The model injected by Spring.
     * @return the path to the view template.
     */
    @RequestMapping("/")
    public String index(final Model model) {
        final List<Article> articles = articles(0);
        model.addAttribute("all", articles);
        model.addAttribute("trending", articleService.getMostViewedThisWeek());
        return PATH;
    }

    /**
     * Get a page (0 based index), by number, of articles to inject into html RESTfully.
     *
     * @param page which page of articles to get (0 indexed).
     * @return a list of articles in the requested page.
     */
    @ResponseBody
    @RequestMapping("/list/{page}")
    public List<Article> articles(@PathVariable final int page) {
        final int from = page < 0 ? 0 : page * ARTICLES_PER_PAGE;
        final int to = from + ARTICLES_PER_PAGE;
        if (from >= 0 && from < ARTICLES.size() && to <= (Integer.MAX_VALUE - ARTICLES_PER_PAGE)) {
            return ARTICLES.subList(from, to > ARTICLES.size() ? ARTICLES.size() : to);
        }
        return Collections.emptyList();
    }
}
