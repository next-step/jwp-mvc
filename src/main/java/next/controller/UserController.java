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
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Controller("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping
    public ModelAndView users(HttpServletRequest request) {
        logger.debug("Request Path : {}", request.getRequestURI());

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView(new RedirectView("/loginForm"));
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new ForwardView("/user/list.jsp"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody User user) {
        logger.debug("User : {}", user);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(@RequestBody User updateUser, HttpServletRequest request) {
        logger.debug("User : {}", updateUser);

        User user = DataBase.findUserById(updateUser.getUserId());
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        user.update(updateUser);
        return new ModelAndView(new RedirectView("/"));
    }

    @RequestMapping(value = "/updateForm")
    public ModelAndView userUpdateForm(String userId, HttpServletRequest request) {
        logger.debug("User Id : {}", userId);

        User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new ForwardView("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/profile/{userId}")
    public ModelAndView userProfile(String userId, HttpServletRequest request) {
        logger.debug("User Id : {}", userId);

        User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        request.setAttribute("user", user);
        return new ModelAndView(new ForwardView("/user/profile.jsp"));
    }
}
