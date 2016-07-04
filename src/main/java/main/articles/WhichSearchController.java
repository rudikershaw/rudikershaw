package main.articles;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/** Controller for the Which Search Algorithm? - Part 1 article. */
@Controller
public class WhichSearchController {

    public static final String PATH = "articles/whichsearch";
    public static final String NAME = "Which Search Algorithm? - Part 1";
    public static final String IMAGE_PATH = "images/search.jpg";
    public static final String DESCRIPTION = "The first of a two part series on searching algorithms. " +
                                             "This article covers exhaustive search and binary search.";

    private ArticleService statisticsService;

    @Autowired
    public WhichSearchController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}