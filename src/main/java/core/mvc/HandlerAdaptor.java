package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {

  boolean isCanHandle(Object handler);

  ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
