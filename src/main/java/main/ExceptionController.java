package main;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** The codenerd Error Controller. */
@Controller
public class ExceptionController implements ErrorController {

    private static final String PATH = "error";

    /** Default mapping for application errors. */
    @RequestMapping(PATH)
    public String error(Model model){
        return PATH;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
