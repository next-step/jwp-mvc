package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.ForwardView;
import core.mvc.view.ModelAndView;
import core.mvc.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(path = "/users")
public class ListUserController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listup(HttpServletRequest request, HttpServletResponse response) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }

        request.setAttribute("users", DataBase.findAll());

        return new ModelAndView(new ForwardView("/user/list.jsp"));
    }
}
