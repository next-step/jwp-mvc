package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.JspView;
import core.mvc.ModelAndView;

@Controller
public class LogoutController {
    
	@RequestMapping(value="/users/logout")
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}
