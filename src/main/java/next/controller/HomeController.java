package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.ForwardView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public ModelAndView forwardHome(HttpServletRequest request) {
        logger.debug("Request Path : {}", request.getRequestURI());

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("home.jsp"));
    }
}
