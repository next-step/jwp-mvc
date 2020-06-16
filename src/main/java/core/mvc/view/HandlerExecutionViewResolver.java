package core.mvc.view;

import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecutionViewResolver implements ViewResolver {

    private final HandlerExecution handlerExecution;

    public HandlerExecutionViewResolver(HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public ModelAndView resolve(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ModelAndView temp = handlerExecution.handle(request, response);
        final String viewName = getJspNameFromRequest(request);
        final JspView jspView = new JspView(viewName);
        final ModelAndView mv = new ModelAndView(jspView);
        temp.getModel().forEach(mv::addObject);
        return mv;
    }

    private String getJspNameFromRequest(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return requestURI.endsWith(".jsp") ? requestURI : requestURI + ".jsp";
    }
}
