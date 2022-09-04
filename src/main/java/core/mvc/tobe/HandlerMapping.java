package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initialize();
    HandlerExecution getHandler(HttpServletRequest request);
}
