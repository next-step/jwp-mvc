package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.ForwardView;
import core.mvc.view.ModelAndView;
import core.mvc.asis.ControllerLegacy;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller(path = "/users")
public class LoginController implements ControllerLegacy {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView forwardLoginForm(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new ForwardView("/user/login.jsp"));
    }
}
