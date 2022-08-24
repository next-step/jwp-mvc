package next.controller;

import core.db.DataBase;
import core.mvc.asis.Controller;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyHomeController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ResourceView("home.jsp"));
    }
}
