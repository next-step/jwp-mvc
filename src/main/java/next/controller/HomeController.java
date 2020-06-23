package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JspView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new JspView("home.jsp"));
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView joinForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }

}