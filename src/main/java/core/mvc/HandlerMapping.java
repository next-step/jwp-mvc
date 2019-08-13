package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping<T> {
	boolean support(HttpServletRequest request);
	
    T getHandler(HttpServletRequest request);
}
