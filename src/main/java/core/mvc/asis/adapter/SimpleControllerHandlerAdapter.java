package core.mvc.asis.adapter;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleControllerHandlerAdapter implements HandlerAdapter{

    @Override
    public boolean support(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public void handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            ModelAndView mv = ((Controller) handler).execute(request, response);
            View view = mv.getView();
            view.render(mv.getModel(), request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
