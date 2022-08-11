package core.mvc.tobe.handler.adapter;

import core.mvc.tobe.view.ModelAndView;
import core.mvc.tobe.view.SimpleNameView;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean support(Object target) {
        return target instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object target) {
        Controller invoker = (Controller) target;
        try {
            String viewName = invoker.execute(request, response);
            return new ModelAndView(new SimpleNameView(viewName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
