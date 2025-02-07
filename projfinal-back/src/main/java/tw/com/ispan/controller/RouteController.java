package tw.com.ispan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

    @GetMapping({ "/", "/index" })
    public String index() {
        return "/index";

    }

    @GetMapping("/secure/login")
    public String page1() {
        return "/secure/login";

    }

    @GetMapping("/secure/loginadmin")
    public String page2() {
        return "/secure/loginadmin";

    }

}
