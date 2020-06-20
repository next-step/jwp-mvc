package core.mvc.view;

import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecutionViewResolver implements ViewResolver {

    @Override
    public boolean isSupports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView resolve(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ModelAndView temp = ((HandlerExecution) handler).handle(request, response);
        final View jspView = temp.getView();
        final ModelAndView mv = new ModelAndView(jspView);
        temp.getModel().forEach(mv::addObject);
        return mv;
    }
}
