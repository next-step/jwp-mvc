package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.tobe.view.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.view.ViewGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView mav = new ModelAndView(ViewGenerator.of("home.jsp"));
        mav.addObject("users", DataBase.findAll());
        return mav;
    }
}
