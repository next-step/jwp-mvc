package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    HandlerExecution getHandler(HttpServletRequest request);
}
