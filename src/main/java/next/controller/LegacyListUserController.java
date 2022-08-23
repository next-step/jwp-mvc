package next.controller;

import core.db.DataBase;
import core.mvc.asis.Controller;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyListUserController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new ResourceView("redirect:/users/loginForm"));
        }

        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ResourceView("/user/list.jsp"));
    }
}
