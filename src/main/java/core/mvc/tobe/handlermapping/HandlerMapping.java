package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handler.HandlerExecution;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    void init();
    HandlerExecution findHandler(HttpServletRequest request);
    boolean hasHandler(HttpServletRequest request);
}
