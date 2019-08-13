package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;

@Controller
public class ListUserController {

	@RequestMapping("/users")
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (!UserSessionUtils.isLogined(req.getSession())) {
			return new ModelAndView(new JspView("redirect:/users/loginForm"));
		}

		ModelAndView modelAndView = new ModelAndView(new JspView("/user/list.jsp"));
		modelAndView.addObject("users", DataBase.findAll());
		return modelAndView;
	}
}
