package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ListUserController {

    @RequestMapping("/users")
    public ModelAndView users(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView("forward:/user/list.jsp");
    }

    @RequestMapping("/users/handlebars")
    public ModelAndView usersByHandlebars(HttpServletRequest request) {
        return new ModelAndView("/user/handlebars-list.hbs")
                .addObject("users", DataBase.findAll());
    }
}
