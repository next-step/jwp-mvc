package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest request) {
        logger.debug("users findUserId");

        if (!UserSessionUtils.isLogined(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("/user/list.jsp"));
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestParam("userId") String asd, @RequestParam String password, @RequestParam String name, @RequestParam String email) {
        logger.debug("users create");

        final User user = new User(asd, password, name, email);

        logger.debug("{}", user);

        DataBase.addUser(user);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, @RequestParam String userId) {
        logger.debug("users findUserId");

        final User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        request.setAttribute("user", user);

        return new ModelAndView(new JspView("/user/profile.jsp"));
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form() {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm() {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest request, @RequestParam String userId) {
        final HttpSession session = request.getSession();
        final User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        request.setAttribute("user", user);

        return new ModelAndView(new JspView("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, @RequestParam String userId, @RequestParam String password, @RequestParam String name, @RequestParam String email) {
        final User user = DataBase.findUserById(userId);

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        final User updateUser = new User(userId, password, name, email);

        logger.debug("Update User : {}", updateUser);

        user.update(updateUser);

        return new ModelAndView(new JspView("redirect:/"));
    }

}
