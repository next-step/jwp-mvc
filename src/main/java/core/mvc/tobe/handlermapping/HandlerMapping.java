package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handler.exception.NotFoundHandlerException;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerExecutions init();
    HandlerExecution findHandler(HttpServletRequest request) throws NotFoundHandlerException;
    boolean hasHandler(HttpServletRequest request);
}
