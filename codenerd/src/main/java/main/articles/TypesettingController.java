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
public class TypesettingController {

    public static final String PATH = "articles/typesetting";
    public static final String NAME = "Taking Typesetting Seriously";
    private ArticleStatisticsService statisticsService;

    @Autowired
    public TypesettingController(ArticleStatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Integer views = statisticsService.incrementViews(PATH, NAME, request);
        model.addAttribute("views", views);
        return PATH;
    }
}
