package main;

import static main.articles.DefaultArticleController.ARTICLES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import main.dynamics.ArticleService;
import main.reddit.RedditPost;
import main.reddit.RedditService;
import main.twitter.TwitterService;

/** Controller for the home page. */
@Controller
public class IndexController {

    /** The path to the index page's view template. */
    private static final String PATH = "index";

    /** The path to the latest tweet fragment. */
    private static final String TWEET_PATH = "fragments/latest-tweet";

    /** Injected article service. */
    private final ArticleService articleService;

    /** Injected twitter service. */
    private final TwitterService twitterService;

    /** Injected reddit service. */
    private final RedditService redditService;

    /**
     * Constructor used to dependency injection.
     *
     * @param injectedArticleService article service to be injected.
     * @param injectedTwitterService twitter service to be injected.
     * @param injectedRedditService reddit service to be injected.
     */
    @Autowired
    public IndexController(final ArticleService injectedArticleService,
                           final TwitterService injectedTwitterService,
                           final RedditService injectedRedditService) {
        this.articleService = injectedArticleService;
        this.twitterService = injectedTwitterService;
        this.redditService = injectedRedditService;
    }

    /**
     * Mapping for the home page.
     *
     * @param model The model injected by Spring.
     * @return the path to the view template.
     */
    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("all", ARTICLES);
        model.addAttribute("trending", articleService.getMostViewedThisWeek());
        return PATH;
    }

    /**
     * Construct a panel containing my latest tweet and return the HTML.
     *
     * @param model the model injected by Spring.
     * @return the path to the latest tweet fragment.
     */
    @GetMapping("/latest-tweet")
    public String latestTweet(final Model model) {
        model.addAttribute("tweet", twitterService.getMyLatestTweet());
        return TWEET_PATH;
    }

    /**
     * Return the latest Reddit post as JSON. The client renders it using
     * textContent, keeping the server free of any HTML injection surface.
     *
     * @return the latest post DTO, or null if unavailable.
     */
    @GetMapping("/latest-reddit-post")
    @ResponseBody
    public RedditPost latestRedditPost() {
        return redditService.getLatestPost();
    }
}
