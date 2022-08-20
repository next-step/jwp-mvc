package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Class<?> declaringClass = method.getDeclaringClass();
        final Constructor<?> constructor = declaringClass.getConstructor();
        final Object instance = constructor.newInstance();
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
