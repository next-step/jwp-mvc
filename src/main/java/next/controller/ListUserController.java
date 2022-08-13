package next.controller;

import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }

        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("/user/list.jsp"));
    }
}
