package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.ViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/users/findUserId")
    public ModelAndView findUserId(final HttpServletRequest request,
                                   final HttpServletResponse response) {
        logger.debug("findUserId");

        return new ModelAndView(ViewFactory.create("/users/show.jsp"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request,
                             final HttpServletResponse response) {
        logger.debug("save");

        return new ModelAndView(ViewFactory.create("redirect:/users"));
    }
}
