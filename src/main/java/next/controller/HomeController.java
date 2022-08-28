package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @RequestMapping("/")
    public ModelAndView homePage(HttpServletRequest req) {
        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ResourceView("home.jsp"));
    }
}
