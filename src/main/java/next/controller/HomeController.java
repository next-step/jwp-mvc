package next.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;

@Controller("/")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req) throws Exception {
        logging("/");

        req.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("home.jsp"));
    }

    private void logging(String requestName) {
        logger.info("Annotation Mapping Handler {} Complete", requestName);
    }
}
