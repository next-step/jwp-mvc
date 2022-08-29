package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}