package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;

/**
 * @author KingCjy
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping
    public ModelAndView home() {
        return new ModelAndView("home.jsp");
    }
}
