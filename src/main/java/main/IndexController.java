package main;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import main.twitter.TwitterService;

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

    /** Injected twitter service. */
    private final TwitterService twitterService;

    /**
     * Constructor used to dependency injection.
     *
     * @param injectedArticleService article service to be injected.
     * @param injectedTwitterService twitter service to be injected.
     *
     */
    @Autowired
    public IndexController(final ArticleService injectedArticleService, final TwitterService injectedTwitterService) {
        this.articleService = injectedArticleService;
        this.twitterService = injectedTwitterService;
    }

    /**
     * Mapping for the home page.
     *
     * @param model The model injected by Spring.
     * @return the path to the view template.
     */
    @RequestMapping("/")
    public String index(final Model model) {
        model.addAttribute("all", ARTICLES);
        model.addAttribute("trending", articleService.getMostViewedThisWeek());
        model.addAttribute("latest", ARTICLES.get(0));
        model.addAttribute("tweet", twitterService.getMyLatestTweet());
        return PATH;
    }
}
