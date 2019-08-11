package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.ModelAndViewHandler;
import next.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @RequestMapping(value = "/users")
    public ModelAndView listUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.listUser(req, resp);
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return ModelAndViewHandler.createModelAndView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView registForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return ModelAndViewHandler.createModelAndView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.createUser(req, resp);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.login(req, resp);
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.profile(req, resp);
    }

    @RequestMapping(value = "/users/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.logout(req, resp);
    }

    @RequestMapping(value = "/users/updateForm")
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.updateForm(req, resp);
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return userService.update(req, resp);
    }
}
