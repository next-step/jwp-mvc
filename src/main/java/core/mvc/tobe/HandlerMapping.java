package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerExecutable getHandler(HttpServletRequest request);
}
