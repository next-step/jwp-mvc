package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView execute(@RequestParam String userId, @RequestParam String password, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        User user = DataBase.findUserById(userId);
        if (user == null) {
            mav.addObject("loginFailed", true);
            mav.setView("/user/login.jsp");
            return mav;
        }
        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            mav.setView("redirect:/");
            return mav;
        } else {
            mav.addObject("loginFailed", true);
            mav.setView("/user/login.jsp");
            return mav;
        }
    }
}
