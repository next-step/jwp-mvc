package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.asis.Controller;

public class ControllerHandlerAdapter extends AbstractHandlerAdapter<Controller>{
	
	public ControllerHandlerAdapter() {
		super(Controller.class);
	}

	@Override
	ModelAndView castHandle(HttpServletRequest request, HttpServletResponse response, Controller handler)
			throws Exception {
		String path = handler.execute(request, response);
		return new ModelAndView(new JspView(path));
	}
}
