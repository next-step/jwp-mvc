package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractHandlerAdapter<T> implements HandlerAdapter{

private final Class<T> parameterizedType;
	
	public AbstractHandlerAdapter(Class<T> parameterizedType) {
		this.parameterizedType = parameterizedType;
	}
	
	@Override
	public boolean supports(Object handler) {
		if(handler == null) {
			return false;
		}
		
		return parameterizedType.isAssignableFrom(handler.getClass());
	}
	
	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return castHandle(request, response, parameterizedType.cast(handler));
	}
	
	abstract ModelAndView castHandle(HttpServletRequest request, HttpServletResponse response, T handler) throws Exception;
}
