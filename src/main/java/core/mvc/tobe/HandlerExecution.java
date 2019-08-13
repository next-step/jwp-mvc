package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface HandlerExecution {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException;
}
