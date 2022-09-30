package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

//    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
//    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse resp) {
//        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
//                req.getParameter("email"));
//        log.debug("User : {}", user);
//        DataBase.addUser(user);
//        return new ModelAndView(new RedirectView("redirect:/"));
//    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser2(User user) {
        log.debug("User : {}", user);
        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("redirect:/"));
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest req, HttpServletResponse resp)  {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return new ModelAndView(new RedirectView("redirect:/users/loginForm"));
        }

        ModelAndView mv = new ModelAndView(new ForwardView("/user/list.jsp"));
        mv.addObject("users", DataBase.findAll());
        return mv;
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView mv = new ModelAndView(new ForwardView("/user/login.jsp"));
            mv.addObject("loginFailed", true);
            return mv;
        }

        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new RedirectView("redirect:/"));
        } else {
            ModelAndView mv = new ModelAndView(new ForwardView("/user/login.jsp"));
            mv.addObject("loginFailed", true);
            return mv;
        }
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

//    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
//    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp)  {
//        String userId = req.getParameter("userId");
//        User user = DataBase.findUserById(userId);
//        if (user == null) {
//            throw new NullPointerException("사용자를 찾을 수 없습니다.");
//        }
//
//        ModelAndView mv = new ModelAndView(new ForwardView("/user/profile.jsp"));
//        mv.addObject("user", user);
//        return mv;
//    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(String userId)  {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        ModelAndView mv = new ModelAndView(new ForwardView("/user/profile.jsp"));
        mv.addObject("user", user);
        return mv;
    }


    @RequestMapping(value = "/users/updateForm", method = RequestMethod.POST)
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)  {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        ModelAndView mv = new ModelAndView(new ForwardView("/user/updateForm.jsp"));
        mv.addObject("user", user);
        return mv;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp)  {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("Update User : {}", updateUser);
        user.update(updateUser);

        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form(HttpServletRequest req, HttpServletResponse resp) {
        return new ModelAndView(new ForwardView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) {
        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }
}
