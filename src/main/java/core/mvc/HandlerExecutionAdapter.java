package core.mvc;

import core.mvc.tobe.HandlerExecution;

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
