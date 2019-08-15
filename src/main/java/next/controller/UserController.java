package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = {"/usersNew", "/userList"}, method = {RequestMethod.GET, RequestMethod.PATCH})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView("/user/list.jsp");
    }

    @RequestMapping(value = "/users/showNew", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView("/user/profile.jsp");
    }

    @RequestMapping("/users/createNew")
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users create");
        return new ModelAndView("redirect:/users");
    }
}
