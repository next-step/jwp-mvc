package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMapping {

    void initialize();

    boolean supports(HttpServletRequest request);

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
