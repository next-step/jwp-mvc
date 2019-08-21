package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView execute() throws Exception {
        ModelAndView modelAndView = new ModelAndView(new JspView("home.jsp"));
        modelAndView.addObject("users", DataBase.findAll());

        return modelAndView;
    }
}
