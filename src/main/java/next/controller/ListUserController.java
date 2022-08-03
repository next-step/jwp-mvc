package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;

@Controller
public class ListUserController {

    @RequestMapping("/users")
    public ModelAndView users(HttpServletRequest request, HttpServletResponse response) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return ModelAndView.withJspView("redirect:/users/loginForm");
        }

        request.setAttribute("users", DataBase.findAll());
        return ModelAndView.withJspView("/user/list.jsp");
    }
}
