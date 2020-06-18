package core.mvc.tobe.handler;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecutionHandler implements Handler<HandlerExecution> {
    @Override
    public ModelAndView handle(HandlerExecution handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handler.handle(request, response);
    }
}
