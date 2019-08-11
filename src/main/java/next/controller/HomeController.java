package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JSPView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView mav = new ModelAndView(new JSPView("home.jsp"));
        mav.addObject("users", DataBase.findAll());
        return mav;
    }
}
