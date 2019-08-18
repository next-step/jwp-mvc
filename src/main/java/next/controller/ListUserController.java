package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class ListUserController {
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView execute(HttpSession session) throws Exception {
        if (!UserSessionUtils.isLogined(session)) {
            return new ModelAndView(new RedirectView("redirect:/users/loginForm"));
        }

        ModelAndView modelAndView = new ModelAndView(new JspView("/user/list.jsp"));
        modelAndView.addObject("users", DataBase.findAll());

        return modelAndView;
    }
}
