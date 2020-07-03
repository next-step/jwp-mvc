package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean supports(HttpServletRequest request);

    HandlerMethod getHandlerMethod(HttpServletRequest request);
}
