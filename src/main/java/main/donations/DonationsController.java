package main.donations;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/** Donations controller. */
@Controller
public class DonationsController {

    /** Mapping for the donations page. */
    @RequestMapping("/donate")
    public String donate(Model model){
        return "donations/donate";
    }

    /** Mapping for the donation cancelled page. */
    @RequestMapping("/donate/cancelled")
    public String cancelled(Model model){
        return "donations/cancelled";
    }

    /** Mapping for the donation successful page. */
    @RequestMapping("donate/thankyou")
    public String thankYou(Model model){
        return "donations/thankyou";
    }
}
