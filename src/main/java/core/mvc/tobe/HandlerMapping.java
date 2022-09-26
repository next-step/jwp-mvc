package core.mvc.tobe;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void initialize();
    HandlerExecution getHandler(HttpServletRequest request) throws ServletException;
}
