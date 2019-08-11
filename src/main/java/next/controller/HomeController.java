package next.controller;

import core.annotation.web.RequestMapping;
import core.db.DataBase;
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
    public String indexMainPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("!!!!!!!!!!~~~~~~~~~!!!!!!!!!!");
        req.setAttribute("users", DataBase.findAll());
        return "home.jsp";
    }
}
