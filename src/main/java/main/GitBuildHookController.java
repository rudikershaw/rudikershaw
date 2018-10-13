package main;

import org.springframework.web.bind.annotation.RequestMapping;

/** Redirects to the Github project so that group-id of the plugin has an existing end-point. */
public class GitBuildHookController {

    /** Request mapping for redirecting from Group ID to the GitHub project. */
    @RequestMapping("/gitbuildhook")
    public String redirectToProject()
    {
        return "redirect:https://github.com/rudikershaw/git-build-hook";
    }
}
