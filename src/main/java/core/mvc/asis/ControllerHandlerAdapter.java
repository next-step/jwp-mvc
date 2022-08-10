package core.mvc.asis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.HandlerAdapter;

public class ControllerHandlerAdapter implements HandlerAdapter {
	@Override
	public boolean supports(Object handler) {
		return handler instanceof Controller;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String forwardView = ((Controller) handler).execute(request, response);
		return new ModelAndView(new JspView(forwardView));
	}
}
