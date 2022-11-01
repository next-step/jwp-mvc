package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView createUser(User user) {
        log.debug("User : {}", user);
        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(HttpSession session)  {
        if (!UserSessionUtils.isLogined(session)) {
            return new ModelAndView(new RedirectView("redirect:/users/loginForm"));
        }

        ModelAndView mv = new ModelAndView(new ForwardView("/user/list.jsp"));
        mv.addObject("users", DataBase.findAll());
        return mv;
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("userId") String userId, String password, HttpSession session) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView mv = new ModelAndView(new ForwardView("/user/login.jsp"));
            mv.addObject("loginFailed", true);
            return mv;
        }

        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new RedirectView("redirect:/"));
        } else {
            ModelAndView mv = new ModelAndView(new ForwardView("/user/login.jsp"));
            mv.addObject("loginFailed", true);
            return mv;
        }
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

//    @RequestMapping(value = "/users/profile/{userId}", method = RequestMethod.GET)
//    public ModelAndView profile(@PathVariable String userId)  {
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
    public ModelAndView profile(User user)  {
        User findUser = DataBase.findUserById(user.getUserId());
        if (findUser == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        ModelAndView mv = new ModelAndView(new ForwardView("/user/profile.jsp"));
        mv.addObject("user", findUser);
        return mv;
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(String userId, HttpSession session)  {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        ModelAndView mv = new ModelAndView(new ForwardView("/user/updateForm.jsp"));
        mv.addObject("user", user);
        return mv;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(User user, HttpSession session)  {
        User findUser = DataBase.findUserById(user.getUserId());
        if (!UserSessionUtils.isSameUser(session, findUser)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(user.getUserId(), user.getPassword(), user.getName(),
                user.getEmail());
        log.debug("Update User : {}", updateUser);
        findUser.update(updateUser);

        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form() {
        return new ModelAndView(new ForwardView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm() {
        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }
}
