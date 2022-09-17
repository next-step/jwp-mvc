package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ForwardView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.asis.Controller;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@core.annotation.web.Controller
public class LoginController implements Controller {

    @Override
    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
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
}
