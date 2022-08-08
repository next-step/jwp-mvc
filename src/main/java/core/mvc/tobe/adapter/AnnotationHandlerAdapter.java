package core.mvc.tobe.adapter;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handlerMapping) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping;
        return new ModelAndView(new JspView((String) handlerExecution.handle(request, response)));
    }

    @Override
    public boolean support(Object object) {
        return (object instanceof HandlerExecution);
    }
}
