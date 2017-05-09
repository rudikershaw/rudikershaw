package main.articles;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for the A PHP Adventure game. */
@Controller
public class PhpAdventureController {

    public static final String PATH = "articles/php-adventure";

    private DefaultArticleController articleController;

    @Autowired
    public PhpAdventureController(DefaultArticleController articleController) {
        this.articleController = articleController;
    }

    @RequestMapping(PATH)
    public String article(Model model, HttpServletRequest request){
        articleController.article("php-adventure", model, request);
        return "redirect:http://rudikershaw.co.nf/subsites/phpadventure/begin.php";
    }
}
