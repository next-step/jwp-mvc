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
import javax.servlet.http.HttpSession;

@Controller(path = "/users")
public class UserLoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = DataBase.findUserById(userId);

        if (user == null || !user.matchPassword(password)) {
            return loginFail(request);
        }

        return loginSuccess(request, user);

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Request Path : {}", request.getRequestURI());

        request.getSession().removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("/"));
    }

    private ModelAndView loginSuccess(HttpServletRequest request, User user) {
        logger.debug("Request Path : {}", request.getRequestURI());

        HttpSession session = request.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return new ModelAndView(new RedirectView("/"));
    }

    private ModelAndView loginFail(HttpServletRequest request) {
        logger.debug("Request Path : {}", request.getRequestURI());

        request.setAttribute("loginFailed", true);
        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }
}
