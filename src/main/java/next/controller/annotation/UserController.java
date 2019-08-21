package next.controller.annotation;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String USER_REQUEST = "/users";

    @RequestMapping(USER_REQUEST)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("/user/list.jsp"));
    }

    @RequestMapping(value = USER_REQUEST, method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"),
                request.getParameter("email"));
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(USER_REQUEST + "/{userId}")
    public ModelAndView profile(HttpServletRequest request, @PathVariable String userId) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new JspView("/user/profile.jsp"));
    }
}
