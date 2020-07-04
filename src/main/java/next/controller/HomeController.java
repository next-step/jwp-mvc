package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.view.JspView;
import org.slf4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class HomeController {

    private static final Logger log = getLogger(HomeController.class);

    @RequestMapping(value = "/")
    public ModelAndView show(HttpServletRequest req, HttpServletResponse resp) {

        req.setAttribute("users", DataBase.findAll());

        ModelAndView mav = new ModelAndView("/home.jsp");
        return mav;
    }
}
