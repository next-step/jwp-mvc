package core.mvc.tobe.resolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;

import core.mvc.tobe.utils.ParameterSupport;
import core.mvc.tobe.utils.TypeUtils;

public class JavabeansHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> clazz = parameter.getType();
        return clazz.getDeclaredConstructors().length == 1;
    }

    @Override
    public Object resolve(String parameterName, Parameter parameter, HttpServletRequest request, Method method) {
        Constructor<?> declaredConstructor = parameter.getType()
            .getDeclaredConstructors()[0];

        var parameterNames = ParameterSupport.nameDiscoverer()
            .getParameterNames(declaredConstructor);
        var parameterTypes = declaredConstructor.getParameterTypes();
        var arguments = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            var constructorParameterName = parameterNames[i];
            Class<?> parameterType = parameterTypes[i];

            var value = request.getParameter(constructorParameterName);

            arguments[i] = TypeUtils.stringToType(parameterType, value);
        }

        try {
            return declaredConstructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException();
        }
    }
}
