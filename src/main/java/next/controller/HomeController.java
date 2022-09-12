package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController extends AbstractController {

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ModelAndView modelAndView = jspView("home.jsp");
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }
}
