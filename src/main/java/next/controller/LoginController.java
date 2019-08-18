package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import next.model.User;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView execute(String userId, String password, HttpSession session) throws Exception {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);
            return new ModelAndView(new JspView("/user/login.jsp"));
        }

        if (user.matchPassword(password)) {
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

            return new ModelAndView(new RedirectView("redirect:/"));
        } else {
            //req.setAttribute("loginFailed", true);
            return new ModelAndView(new JspView("/user/login.jsp")).addObject("loginFailed", true);
        }
    }
}
