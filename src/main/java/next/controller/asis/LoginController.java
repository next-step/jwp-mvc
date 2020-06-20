package next.controller.asis;

import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import next.controller.UserSessionUtils;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/"));
        } else {
            ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
    }
}
