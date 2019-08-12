package next.controller;

import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.asis.DispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@core.annotation.web.Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @RequestMapping("/")
    public ModelAndView indexMainPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView("home.jsp");
    }
}
