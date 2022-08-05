package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    @RequestMapping("/")
    public ModelAndView welcome(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return ModelAndView.newInstance("home.jsp");
    }

    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) {
        return ModelAndView.newInstance("redirect:/");
    }
}
