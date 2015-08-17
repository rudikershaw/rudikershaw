package main.articles;

import main.dynamics.ArticleStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class HttpBasicsController {

    public static final String PATH = "articles/httpbasics";
    public static final String NAME = "HTTP Basics";
    private ArticleStatisticsService statisticsService;

    @Autowired
    public HttpBasicsController(ArticleStatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Integer views = statisticsService.incrementViews(PATH, NAME, request);
        model.addAttribute("views", views);
        return PATH;
    }
}
