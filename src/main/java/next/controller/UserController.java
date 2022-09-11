package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );

        logger.debug("User : {}", user);
        DataBase.addUser(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView("/user/list.jsp");
    }
}
