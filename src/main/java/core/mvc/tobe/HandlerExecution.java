package core.mvc.tobe;

import core.mvc.ModelAndView;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution {

	private final Object instance;
	private final Method method;

	public HandlerExecution(Object instance, Method method) {
		this.instance = instance;
		this.method = method;
	}

	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return (ModelAndView) method.invoke(instance, request, response);
	}
}
