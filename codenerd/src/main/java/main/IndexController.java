package main;

import main.dynamics.ArticleStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class IndexController {

    ArticleStatisticsService statisticsService;

    @Autowired
    public IndexController(ArticleStatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping("/")
    public String index(Model model){
        statisticsService.getAllArticles();
        return "index";
    }
}
