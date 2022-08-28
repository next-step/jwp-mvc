package core.mvc.adapter;

import core.mvc.asis.Controller;
import core.mvc.view.ModelAndView;
import core.mvc.view.ResourceView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String viewPath = ((Controller) handler).execute(request, response);
        return new ModelAndView(new ResourceView(viewPath));
    }
}
