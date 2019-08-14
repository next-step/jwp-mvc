package core.mvc;

import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandler extends AbstractHandlerAdapter {

    public AnnotationHandler(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
