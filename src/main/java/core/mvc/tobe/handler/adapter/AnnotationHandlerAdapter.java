package core.mvc.tobe.handler.adapter;

import core.mvc.tobe.view.ModelAndView;
import core.mvc.tobe.handler.mapping.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean support(Object target) {
        return target instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object target) {
        HandlerExecution invoker = (HandlerExecution) target;
        try {
            return invoker.handle(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
