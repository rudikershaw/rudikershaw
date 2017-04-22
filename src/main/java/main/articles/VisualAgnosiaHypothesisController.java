package main.articles;

import javax.servlet.http.HttpServletRequest;
import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for the Visual Agnosia Hypothesis. */
@Controller
public class VisualAgnosiaHypothesisController {

    public static final String PATH = "articles/visual-agnosia";
    public static final String NAME = "Visual Agnosia Hypothesis";
    public static final String IMAGE_PATH = "images/visual-agnosia.jpg";
    public static final String DESCRIPTION = "Reading Oliver Sacks' got me thinking about how artificial " +
                                             "neural nets process images.";

    private ArticleService statisticsService;

    @Autowired
    public VisualAgnosiaHypothesisController(ArticleService statisticsService){
        this.statisticsService = statisticsService;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        Article article = statisticsService.initialise(PATH, NAME, IMAGE_PATH, DESCRIPTION, request);
        model.addAttribute("article", article);
        return PATH;
    }

}