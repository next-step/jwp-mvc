package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewResolver {
    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
