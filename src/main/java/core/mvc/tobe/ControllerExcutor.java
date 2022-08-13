package core.mvc.tobe;

import java.lang.reflect.Method;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerExcutor implements Controller {
    private final Object declaredObject;
    private final Method method;

    public ControllerExcutor(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(declaredObject, request, response);
    }
}
