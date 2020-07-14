package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean getHandlerAdapter(final Object handler);

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception;
}
