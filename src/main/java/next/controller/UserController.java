package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
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
	public ModelAndView login(HttpServletRequest req, @RequestParam String userId, @RequestParam String password) {
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
	public ModelAndView profile(HttpServletRequest req, @RequestParam String userId) {
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
	public ModelAndView create(HttpServletRequest req, User user) {
		DataBase.addUser(user);
		return new ModelAndView(new JspView("redirect:/"));
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpServletRequest req, @RequestParam String userId) {
		User user = DataBase.findUserById(userId);
		if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}
		req.setAttribute("user", user);
		return new ModelAndView(new JspView("/user/updateForm.jsp"));
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest req, HttpServletResponse resp, User user) {
		User findUser = DataBase.findUserById(user.getUserId());
		if (!UserSessionUtils.isSameUser(req.getSession(), findUser)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}

		User updateUser = new User(findUser.getUserId(), user.getPassword(), user.getName(), user.getEmail());
		user.update(updateUser);
		return new ModelAndView(new JspView("redirect:/"));
	}
}
