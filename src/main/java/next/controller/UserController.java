package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpSession httpSession) {
        logger.debug("users findUserId");

        if (!UserSessionUtils.isLogined(httpSession)) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }

        ModelAndView modelAndView = new ModelAndView(new JspView("/user/list.jsp"));
        modelAndView.addObject("users", DataBase.findAll());

        return modelAndView;
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(User user) {
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }
}
