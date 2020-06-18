package core.mvc.tobe.handler;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler<T> {
    ModelAndView handle(T handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
