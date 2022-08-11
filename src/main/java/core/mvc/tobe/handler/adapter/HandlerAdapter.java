package core.mvc.tobe.handler.adapter;

import core.mvc.tobe.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean support(Object target);
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object target);
}
