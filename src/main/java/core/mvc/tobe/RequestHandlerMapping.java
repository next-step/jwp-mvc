package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

public interface RequestHandlerMapping {
    void initialize();

    HandlerCommand getHandler(HttpServletRequest request);
}
