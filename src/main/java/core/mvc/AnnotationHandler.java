package core.mvc;

import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandler implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        assert handler != null;

        return HandlerExecution.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
