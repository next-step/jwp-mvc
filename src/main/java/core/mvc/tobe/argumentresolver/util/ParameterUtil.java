package core.mvc.tobe.argumentresolver.util;

import core.mvc.tobe.argumentresolver.MethodArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodArgumentResolvers;
import core.mvc.tobe.argumentresolver.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterUtil {
    public static Object[] getMethodParameters(Method method, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Parameter[] parameters = method.getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i=0; i<parameters.length; i++){
            MethodParameter methodParameter = new MethodParameter(method, i);
            MethodArgumentResolver resolver = MethodArgumentResolvers.findResolver(methodParameter);
            objects[i] = resolver.resolve(methodParameter, request, response);
        }
        return objects;
    }
}
