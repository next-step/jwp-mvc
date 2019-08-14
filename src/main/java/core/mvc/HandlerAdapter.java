package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
	
	
	public boolean supports(Object handler);
	
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception ;
}
