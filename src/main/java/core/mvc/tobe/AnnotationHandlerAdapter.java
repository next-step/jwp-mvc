package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean support(Object object) {
        return object instanceof HandlerExecution;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution)handler;
        ModelAndView modelAndView = handlerExecution.handle(req, resp);
        modelAndView.viewRender(req, resp);
    }
}
