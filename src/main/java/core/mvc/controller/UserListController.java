package core.mvc.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import next.controller.UserSessionUtils;

@Controller
public class UserListController {

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest req) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {

            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }

        ModelAndView modelAndView = new ModelAndView(new JspView("/user/list.jsp"));
        modelAndView.addObject("users", DataBase.findAll());

        return modelAndView;
    }

}
