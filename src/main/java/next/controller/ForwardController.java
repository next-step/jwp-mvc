package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.JspView;
import core.mvc.ModelAndView;

@Controller
public class ForwardController {

	@RequestMapping(value="/users/form")
	public ModelAndView userFormFoward(HttpServletRequest req, HttpServletResponse resp) {
		return new ModelAndView(new JspView("/user/form.jsp"));
	}
	
	@RequestMapping(value="/users/loginForm")
	public ModelAndView userLoginFormForward(HttpServletRequest req, HttpServletResponse resp) {
		return new ModelAndView(new JspView("/user/login.jsp"));
	}
}
