package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import core.mvc.tobe.ForwardView;
import core.mvc.tobe.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(path = "/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView(new RedirectView("/users/loginForm"));
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("/user/list.jsp"));
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new ForwardView("/user/profile.jsp"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView usersForm(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        return new ModelAndView(new ForwardView("/user/form.jsp"));
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView usersLoginForm(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }
}
