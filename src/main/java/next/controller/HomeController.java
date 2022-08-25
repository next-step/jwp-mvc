package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.web.view.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = ModelAndView.getJspModelAndView("home.jsp");
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }

}
