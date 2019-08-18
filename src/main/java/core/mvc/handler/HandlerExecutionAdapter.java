package core.mvc.handler;

import core.mvc.ModelAndView;
import core.mvc.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public void handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);

        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
