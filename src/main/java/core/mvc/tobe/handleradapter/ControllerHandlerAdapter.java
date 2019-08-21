package core.mvc.tobe.handleradapter;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import core.mvc.tobe.ModelAndViewWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ModelAndViewWrapper.wrap(((Controller) handler).execute(request, response), JspView.class);
    }
}
