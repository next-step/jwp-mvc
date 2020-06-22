package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("home.jsp"));
    }

}
