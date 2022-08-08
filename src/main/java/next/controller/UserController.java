package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.model.User;

@Controller(value = "/users")
public class UserController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView users(HttpServletRequest req, HttpServletResponse resp) {
		if (!UserSessionUtils.isLogined(req.getSession())) {
			return new ModelAndView(new JspView("redirect:/users/loginForm"));
		}

		req.setAttribute("users", DataBase.findAll());
		return new ModelAndView(new JspView("/user/list.jsp"));
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(HttpServletRequest req, HttpServletResponse resp) {
		return new ModelAndView(new JspView("/user/form.jsp"));
	}

	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) {
		return new ModelAndView(new JspView("/user/login.jsp"));
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		User user = DataBase.findUserById(userId);
		if (user == null) {
			req.setAttribute("loginFailed", true);
			return new ModelAndView(new JspView("/user/login.jsp"));
		}
		if (user.matchPassword(password)) {
			HttpSession session = req.getSession();
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
			return new ModelAndView(new JspView("redirect:/"));
		} else {
			req.setAttribute("loginFailed", true);
			return new ModelAndView(new JspView("/user/login.jsp"));
		}
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) {
		String userId = req.getParameter("userId");
		User user = DataBase.findUserById(userId);
		if (user == null) {
			throw new NullPointerException("사용자를 찾을 수 없습니다.");
		}
		req.setAttribute("user", user);
		return new ModelAndView(new JspView("/user/profile.jsp"));
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
		return new ModelAndView(new JspView("redirect:/"));
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) {
		User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
							 req.getParameter("email"));
		DataBase.addUser(user);
		return new ModelAndView(new JspView("redirect:/"));
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp) {
		String userId = req.getParameter("userId");
		User user = DataBase.findUserById(userId);
		if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		req.setAttribute("user", user);
		return new ModelAndView(new JspView("/user/updateForm.jsp"));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) {
		User user = DataBase.findUserById(req.getParameter("userId"));
		if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}

		User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
								   req.getParameter("email"));
		user.update(updateUser);
		return new ModelAndView(new JspView("redirect:/"));
	}
}
