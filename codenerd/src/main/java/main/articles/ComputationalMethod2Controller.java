package main.articles;

import main.dynamics.ArticleStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class ComputationalMethod2Controller {

    public static final String PATH = "articles/computationalmethod2";
    public static final String NAME = "Defining an Algorithm";
    private ArticleStatisticsService statisticsService;

    @Autowired
    public ComputationalMethod2Controller(ArticleStatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model){
        Integer views = statisticsService.incrementViews(PATH, NAME);
        model.addAttribute("views", views);
        return PATH;
    }
}
