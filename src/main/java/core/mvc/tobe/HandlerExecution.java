package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface HandlerExecution {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
