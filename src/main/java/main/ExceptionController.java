package main;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Rudi Kershaw on 23/08/2015.
 */
@Controller
public class ExceptionController implements ErrorController {

    private static final String PATH = "error";

    @RequestMapping(PATH)
    public String error(Model model){

        return PATH;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
