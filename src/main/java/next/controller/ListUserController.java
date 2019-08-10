package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ListUserController {

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest req, HttpServletResponse resp) {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            RedirectView redirectView = new RedirectView("redirect:/users/loginForm");
            return new ModelAndView(redirectView);
        }

        JspView jspView = new JspView("/user/list");
        ModelAndView mav = new ModelAndView(jspView);
        mav.addObject("users", DataBase.findAll());
        return mav;
    }
}
