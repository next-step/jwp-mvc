package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.asis.Controller;
import core.mvc.tobe.HandlerExecution;

public class ExecutionHandlerAdapter extends HandlerAdapter<HandlerExecution>{
	
	public ExecutionHandlerAdapter() {
		super(HandlerExecution.class);
	}

	@Override
	ModelAndView castHandle(HttpServletRequest request, HttpServletResponse response, HandlerExecution handler)
			throws Exception {
		return handler.handle(request, response);
	}
}
