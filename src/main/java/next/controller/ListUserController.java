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
    public ModelAndView getUsers(HttpServletRequest request,
                                 HttpServletResponse response) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView("/user/list.jsp");
    }

}
