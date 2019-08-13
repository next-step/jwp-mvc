package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class HandlerAdapter<T> {
	
	private final Class<T> parameterizedType;
	
	public HandlerAdapter(Class<T> parameterizedType) {
		this.parameterizedType = parameterizedType;
	}
	
	public boolean supports(Object handler) {
		if(handler == null) {
			return false;
		}
		
		return parameterizedType.isAssignableFrom(handler.getClass());
	}
	
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return castHandle(request, response, parameterizedType.cast(handler));
	}
	
	abstract ModelAndView castHandle(HttpServletRequest request, HttpServletResponse response, T handler) throws Exception;
}
