package main;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Rudi Kershaw on 20/06/2015.
 */
@RestController
public class IndexController {

    @RequestMapping("/")
    public String home(){
        return "So far so good!";
    }
}
