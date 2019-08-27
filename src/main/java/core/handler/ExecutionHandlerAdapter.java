package core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;

public class ExecutionHandlerAdapter extends AbstractHandlerAdapter<HandlerExecution>{
	
	public ExecutionHandlerAdapter() {
		super(HandlerExecution.class);
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return ((HandlerExecution) handler).handle(request, response);
	}
}
