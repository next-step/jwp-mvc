package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean canHandle(Object target);
    ModelAndView handle(Object target, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
