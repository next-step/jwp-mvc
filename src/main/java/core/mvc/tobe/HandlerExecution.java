package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Class<?> controllerClass;
    private final Method declaredMethods;

    public HandlerExecution(Class<?> controllerClass, Method declaredMethods) {
        this.controllerClass = controllerClass;
        this.declaredMethods = declaredMethods;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Constructor<?> constructor = controllerClass.getConstructor();
        return (ModelAndView) declaredMethods.invoke(constructor.newInstance(), request, response);
    }
}
