package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;

import core.mvc.view.JspView;
import core.mvc.ModelAndView;
@Controller
public class HomeController  {

    @RequestMapping("/")
    public ModelAndView execute() {
        ModelAndView modelAndView = new ModelAndView(new JspView("/home.jsp"));
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }
}
