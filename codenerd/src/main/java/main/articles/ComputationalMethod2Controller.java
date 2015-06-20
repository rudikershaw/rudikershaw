package main.articles;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class ComputationalMethod2Controller {

    @RequestMapping("articles/computationalmethod2")
    public String article(Model model){
        return "articles/computationalmethod2";
    }
}
