package core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;

public class ControllerHandlerAdapter extends AbstractHandlerAdapter<Controller>{
	
	public ControllerHandlerAdapter() {
		super(Controller.class);
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = ((Controller) handler).execute(request, response);
		return new ModelAndView(new JspView(path));
	}
}
