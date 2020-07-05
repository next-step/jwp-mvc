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

    private static final Logger logger = getLogger("HomeController");

    @RequestMapping(value = "/")
    public ModelAndView home(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());

        ModelAndView mav = new ModelAndView(new JspView("home"));

        return mav;
    }
}
