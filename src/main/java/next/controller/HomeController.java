package next.controller;

import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.view.ForwardView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("home.jsp"));
    }
}
