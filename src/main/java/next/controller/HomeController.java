package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    @RequestMapping("/")
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView modelAndView = ModelAndView.from("home.jsp");
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }
}
