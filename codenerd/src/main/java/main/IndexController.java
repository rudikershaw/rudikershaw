package main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(Model model){
        return "index";
    }
}
