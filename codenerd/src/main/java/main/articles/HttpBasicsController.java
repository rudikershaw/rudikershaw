package main.articles;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class HttpBasicsController {

    @RequestMapping("articles/httpbasics")
    public String article(Model model){
        return "articles/httpbasics";
    }
}
