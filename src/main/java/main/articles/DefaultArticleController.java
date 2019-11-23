package main.articles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;

/** Controller for the getting arbitrary articles. */
@Controller
public class DefaultArticleController {

    /**
     * Ordered unmodifiable list containing details of each published article.
     * New articles must be added to this list for them to be accessible.
     * Order of the list determines the order of the articles on the site.
     */
    @SuppressWarnings(value = "MS_MUTABLE_COLLECTION")
    public static final List<Article> ARTICLES;

    static {
        List<Article> list = new ArrayList<>();
        //CHECKSTYLE:OFF
        list.add(new Article("Defining an Algorithm", "articles/computationalmethod", "images/taocp.jpg", "The first of a series of posts on interpreting Donald Knuth's famous volumes; The Art of Computer Programming."));
        list.add(new Article("Taking Typesetting Seriously", "articles/typesetting", "images/typesetting.jpg", "In 'Taking Typesetting Seriously' I talk about the @font-face css and how you can declare custom fonts for your website."));
        list.add(new Article("HTTP Basics", "articles/httpbasics", "images/http.jpg", "Having realised that I didn't know anywhere near enough about HTTP, I put this together as a reference. Have a read."));
        list.add(new Article("Defining an Algorithm 2", "articles/computationalmethod2", "images/taocp.jpg", "The second of a series of posts on interpreting Donald Knuth's famous volumes; The Art of Computer Programming."));
        list.add(new Article("Upgrading This Website", "articles/website-upgrade", "images/websiteupgrade.jpg", "After months of inactivity I upgraded this site to use JavaEE, Spring boot, as well as a MySQL database for some fun extras."));
        list.add(new Article("Defining an Algorithm 3", "articles/computationalmethod3", "images/taocp.jpg", "The third of a series of posts on interpreting Donald Knuth's famous volumes; The Art of Computer Programming."));
        list.add(new Article("Which Search Algorithm? - Part 1", "articles/whichsearch", "images/search.jpg", "The first of a two part series on searching algorithms. This article covers exhaustive search and binary search."));
        list.add(new Article("Event Driven Java EE7", "articles/java-essentials", "images/events.jpg", "After discovering event based annotations I talk through a quick review of the ways to use event driven programming in Java EE7."));
        list.add(new Article("The Trouble with Science", "articles/science-trouble", "images/science-trouble.jpg", "Science is how we know what is true and what is not. This article covers the biggest problems that science faces right now."));
        list.add(new Article("Whichpet", "articles/whichpet", "images/whichpet-orange.png", "Whichpet is a Javascript library for classifying text descriptions into labels. In this case pet descriptions and the type of pets."));
        list.add(new Article("Which Search Algorithm? - Part 2", "articles/whichsearch2", "images/search.jpg", "The 2nd of a two part series on searching algorithms. This article covers depth and breadth first searches."));
        list.add(new Article("Nature vs Technology", "articles/nature-vs-technology", "images/nature.jpg", "Is nature really at odds with science and technology? The ramblings of a science enthusiast gone camping."));
        list.add(new Article("A Tour of AI", "articles/tour-of-ai", "images/tour-of-ai.jpg", "It's easy to feel like modern AI is just magic. It isn't, and here is how it works and what all the buzz words mean."));
        list.add(new Article("Visual Agnosia Hypothesis", "articles/visual-agnosia", "images/visual-agnosia.jpg", "Reading Oliver Sacks' on agnosia got me thinking about how artificial neural networks process images."));
        list.add(new Article("Anomaly Detection", "articles/anomaly-detection", "images/anomaly-detection.jpg", "A machine learning example and explanation of an anomaly detection system for detecting suspicious user activity."));
        list.add(new Article("Grokking RSA Encryption", "articles/rsa-encryption", "images/rsa-encryption.jpg", "Finally put in the effort to really understand public key encryption in the form of RSA. Hopefully this helps you too."));
        list.add(new Article("A Year & 20 Something Books", "articles/20-something-books", "images/20-something-books.jpg", "A little over a year ago I set myself the goal of reading a book every 2 weeks, and this is what I learned and how it went."));
        list.add(new Article("There Is No AI Apocalypse", "articles/ai-doom-isnt-coming", "images/ai-doom-isnt-coming.jpg", "A lot of very smart people seem to believe that AI is going to kill us all if we're not careful. Here's why they're wrong."));
        list.add(new Article("Git Hooks and Java Projects", "articles/git-hooks-and-java", "images/git-hooks.jpg", "From hearing about everyone's project setups it seems Git hooks are widely under-utilised. I believe we can do better."));
        //CHECKSTYLE:ON
        Collections.reverse(list);
        ARTICLES = Collections.unmodifiableList(list);
    }

    /** Injected article service. */
    private final ArticleService statisticsService;

    /** Base URL from the properties file. */
    @Value("${base.url}")
    private String baseUrl;

    /**
     * Constructor used for autowiring.
     * @param articleService autowired statistics service.
     */
    @Autowired
    public DefaultArticleController(final ArticleService articleService) {
        this.statisticsService = articleService;
    }

    /**
     * Mapping for articles taking the path postfix as a parameter.
     * @param path postfix for the path to the article.
     * @param model model for populating and passing to view layer.
     * @param request the request.
     * @return the path to the view template.
     */
    @RequestMapping("articles/{path}")
    public String article(@PathVariable final String path, final Model model, final HttpServletRequest request) {
        // Retrieve article from list.
        final Article a = ARTICLES.stream().filter(art -> art.getPath().equals("articles/" + path)).findAny()
                                           .orElseThrow(EntityNotFoundException::new);

        final Article article = statisticsService.initialise(a.getPath(), a.getName(), a.getImagePath(), a.getDescription(), request);
        // Get next & previous article if any.
        Article next = null;
        Article previous = null;
        final int index = ARTICLES.indexOf(a);
        if (index > 0) {
            previous = ARTICLES.get(index - 1);
        }
        if (index < ARTICLES.size() - 1) {
            next = ARTICLES.get(index + 1);
        }
        // Add to model.
        model.addAttribute("article", article);
        model.addAttribute("next", next);
        model.addAttribute("previous", previous);
        model.addAttribute("baseUrl", baseUrl);
        return a.getPath();
    }
}
