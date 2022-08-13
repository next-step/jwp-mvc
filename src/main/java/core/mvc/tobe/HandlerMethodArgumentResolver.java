package core.mvc.tobe;

import core.mvc.tobe.exception.InvalidInstanceException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class HandlerMethodArgumentResolver {

    private static final ParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final Object[] EMPTY_PARAMETERS = new Object[0];

    public Object[] resolve(final Method method, final HttpServletRequest request) {
        final String[] parameterNames = DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            return EMPTY_PARAMETERS;
        }

        return getParameters(method.getParameterTypes(), parameterNames, request);
    }

    private Object[] getParameters(final Class<?>[] parameterTypes, final String[] parameterNames, final HttpServletRequest request) {
        final Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            parameters[i] = getParameter(parameterType, parameterNames[i], request);
        }

        return parameters;
    }

    private Object getParameter(final Class<?> parameterType, final String parameterName, final HttpServletRequest request) {
        if (parameterType.isPrimitive() || String.class == parameterType) {
            return getValueWithMatchingType(parameterType, request.getParameter(parameterName));
        }
        return getNewInstance(parameterType, request);
    }

    private Object getNewInstance(final Class<?> parameterType, final HttpServletRequest request) {
        final Object instance = getNewInstance(parameterType);

        final Field[] declaredFields = parameterType.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            final String parameter = request.getParameter(declaredField.getName());
            setField(instance, declaredField, parameter);
        }
        return instance;
    }

    private static void setField(final Object instance, final Field declaredField, final String parameter) {
        if (Objects.isNull(parameter) || parameter.isBlank()) {
            return;
        }

        try {
            declaredField.setAccessible(true);
            declaredField.set(instance, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getValueWithMatchingType(final Class<?> parameterType, final String parameter) {
        if (Objects.isNull(parameter) || parameter.isBlank()) {
            return getDefaultValue(parameterType);
        }
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(parameter);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(parameter);
        }
        return parameter;
    }

    private static Object getNewInstance(final Class<?> parameterType) {
        final Constructor<?>[] declaredConstructors = parameterType.getDeclaredConstructors();
        if (declaredConstructors.length == 0) {
            throw new InvalidInstanceException(new NoSuchMethodException());
        }

        final Constructor<?> declaredConstructor = declaredConstructors[0];
        final Object[] constructorArguments = Arrays.stream(declaredConstructor.getParameterTypes())
            .map(HandlerMethodArgumentResolver::getDefaultValue)
            .toArray();

        try {
            return declaredConstructor.newInstance(constructorArguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InvalidInstanceException(e);
        }
    }

    private static Object getDefaultValue(Class<?> clazz) {
        return Array.get(Array.newInstance(clazz, 1), 0);
    }

}
