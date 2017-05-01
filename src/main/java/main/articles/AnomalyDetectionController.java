package main.articles;

import javax.servlet.http.HttpServletRequest;
import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for the Anomaly Detection article. */
@Controller
public class AnomalyDetectionController {

    public static final String PATH = "articles/anomaly-detection";
    public static final String NAME = "Anomaly Detection";
    public static final String IMAGE_PATH = "images/anomaly-detection.jpg";
    public static final String DESCRIPTION = "A machine learning example and explanation of an anomaly detection " +
                                             "system for detecting suspicious user activity.";

    private ArticleService statisticsService;

    @Autowired
    public AnomalyDetectionController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}