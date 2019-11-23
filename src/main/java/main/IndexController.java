package main;

import static main.articles.DefaultArticleController.ARTICLES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import main.dynamics.ArticleService;
import main.twitter.TwitterService;

/** Controller for the home page. */
@Controller
public class IndexController {

    /** The path to the index page's view template. */
    private static final String PATH = "index";

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
