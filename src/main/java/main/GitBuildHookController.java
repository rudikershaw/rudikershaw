package main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/** Redirects to the Github project so that group-id of the plugin has an existing end-point. */
@Controller
class GitBuildHookController {

    /**
     * Request mapping for redirecting from Group ID to the GitHub project.
     *
     * @return a retidrect path to the git build hook project.
     */
    @RequestMapping(value = "/gitbuildhook", method = {RequestMethod.GET, RequestMethod.POST})
    public String redirectToProject() {
        return "redirect:https://github.com/rudikershaw/git-build-hook";
    }
}
