package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.JspView;
import core.mvc.ModelAndView;
import core.mvc.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.model.User;

@Controller("/users")
public class LoginController {

    @RequestMapping("/form")
    public ModelAndView form(HttpServletRequest req, HttpServletResponse resp){
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping("/loginForm")
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp){
        return new ModelAndView(new JspView("/user/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp){
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null || !user.matchPassword(password)) {
            ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }

        HttpSession session = req.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return new ModelAndView(new RedirectView("/"));
    }


    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp){
        return new ModelAndView(new RedirectView("/"));
    }

}
