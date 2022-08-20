package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Class<?> controllerClass;
    private final Method declaredMethods;

    // 임시 생성자 생성
    public HandlerExecution(Object controllerClass, Method declaredMethods) {
        this.controllerClass = null;
        this.declaredMethods = declaredMethods;
    }

    public HandlerExecution(Class<?> controllerClass, Method declaredMethods) {
        this.controllerClass = controllerClass;
        this.declaredMethods = declaredMethods;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Constructor<?> constructor = controllerClass.getConstructor();
        return getModelAndView(declaredMethods.invoke(constructor.newInstance(), request, response));
    }

    private ModelAndView getModelAndView(Object invokeObject) {
        if (invokeObject instanceof ModelAndView) {
            return (ModelAndView) invokeObject;
        }

        if (invokeObject instanceof String) {
            return new ModelAndView((String) invokeObject);
        }
        return null;
    }
}
