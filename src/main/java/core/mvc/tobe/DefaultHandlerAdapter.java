package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(Object target) {
        return target instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object target, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handler = (HandlerExecution) target;
        return handler.handle(request, response);
    }
}
