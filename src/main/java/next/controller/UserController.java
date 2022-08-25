package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.web.view.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView userForm() {
        return ModelAndView.getJspModelAndView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView userLoginForm() {
        return ModelAndView.getJspModelAndView("/user/login.jsp");
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest request) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return ModelAndView.getJspModelAndView("redirect:/users/loginForm");
        }

        ModelAndView modelAndView = ModelAndView.getJspModelAndView("/user/list.jsp");
        modelAndView.addObject("users", DataBase.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(String userId, String password, HttpServletRequest request) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView modelAndView = ModelAndView.getJspModelAndView("/user/login.jsp");
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
        if (user.matchPassword(password)) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return ModelAndView.getJspModelAndView("redirect:/");
        } else {
            ModelAndView modelAndView = ModelAndView.getJspModelAndView("/user/login.jsp");
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView profile(String userId) {
        ModelAndView modelAndView = ModelAndView.getJspModelAndView("/user/profile.jsp");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return ModelAndView.redirectHome();
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView user(@RequestBody User user) {
        DataBase.addUser(user);
        return ModelAndView.redirectHome();
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(String userId, HttpServletRequest request) {
        ModelAndView modelAndView = ModelAndView.getJspModelAndView("/user/updateForm.jsp");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(String userId, @RequestBody User updateUser, HttpServletRequest request) {
        User user = DataBase.findUserById(request.getParameter(userId));
        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        user.update(updateUser);

        return ModelAndView.redirectHome();
    }

    @RequestMapping(value = "/users/{test}", method = RequestMethod.GET)
    public ModelAndView pathVariableTest(@PathVariable String test) {
        System.out.println(test);
        return ModelAndView.redirectHome();
    }

}
