package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView base(HttpServletRequest req, HttpServletResponse resp) {
        return goToHomeJsp(req);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) {
        return goToHomeJsp(req);
    }

    private ModelAndView goToHomeJsp(HttpServletRequest req) {
        req.setAttribute("users", DataBase.findAll());
        JspView jspView = new JspView("home.jsp");
        return new ModelAndView(jspView);
    }

}
