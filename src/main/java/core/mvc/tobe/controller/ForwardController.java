package core.mvc.tobe.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.JspView;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ForwardController {

    @RequestMapping(value = "/users/form")
    public ModelAndView userForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("redirect:/user/form"));
    }

    @RequestMapping(value = "/users/loginForm")
    public ModelAndView userLoginForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/user/login"));
    }
}
