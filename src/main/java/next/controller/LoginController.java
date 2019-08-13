package next.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

@Controller
public class LoginController {

	@RequestMapping(value="/users/login")
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		User foundUser = DataBase.findUserById(userId);

		boolean isUserMatched = Optional.ofNullable(foundUser)
				.filter(user -> user.matchPassword(password))
				.isPresent();

		if (isUserMatched) {
			HttpSession session = req.getSession();
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, foundUser);
			return new ModelAndView(new JspView("redirect:/"));
		}

		ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
		modelAndView.addObject("loginFailed", true);
		return modelAndView;

	}
}
