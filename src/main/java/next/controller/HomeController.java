package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.view.JspView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ModelAndView modelAndView = new ModelAndView(new JspView("home.jsp"));
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }
}
