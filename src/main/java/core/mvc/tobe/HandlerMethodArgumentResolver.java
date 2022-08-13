package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.exception.InvalidInstanceException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

public class HandlerMethodArgumentResolver {

    private static final ParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final Object[] EMPTY_PARAMETERS = new Object[0];

    public Object[] resolve(final Method method, final HttpServletRequest request) {
        if (method == null) {
            return EMPTY_PARAMETERS;
        }
        final String[] parameterNames = DISCOVERER.getParameterNames(method);
        if (parameterNames == null) {
            return EMPTY_PARAMETERS;
        }
        return getParameters(method, parameterNames, request);
    }

    private Object[] getParameters(final Method method, final String[] parameterNames, final HttpServletRequest request) {

        final Parameter[] parameters = method.getParameters();
        final Object[] params = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = getParameter(method, parameters[i], parameterNames[i], request);
        }

        return params;
    }

    private Object getParameter(final Method method, final Parameter parameter, final String parameterName, final HttpServletRequest request) {
        final Class<?> parameterType = parameter.getType();

        if (HttpServletRequest.class == parameterType) {
            return request;
        }

        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return getPathVariableValue(method, parameter, parameterName, request);
        }

        if (parameterType.isPrimitive() || String.class == parameterType || isPrimitiveWrapper(parameterType)) {
            return getValueWithMatchingType(parameterType, request.getParameter(parameterName));
        }
        return getNewInstance(parameterType, request);
    }

    private Object getPathVariableValue(final Method method, final Parameter parameter, final String parameterName, final HttpServletRequest request) {
        final PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final PathPatternParser parser = new PathPatternParser();
        final PathPattern pathPattern = parser.parse(requestMapping.value());
        final PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(PathContainer.parsePath(request.getRequestURI()));
        if (Objects.isNull(pathMatchInfo)) {
            return getValueWithMatchingType(parameter.getType(), null);
        }
        final Map<String, String> uriVariables = pathMatchInfo.getUriVariables();
        final String pathValue = pathVariable.value();
        if (Objects.nonNull(pathValue) && !pathValue.isBlank()) {
            return getValueWithMatchingType(parameter.getType(), uriVariables.get(pathValue));
        }
        return getValueWithMatchingType(parameter.getType(), uriVariables.get(parameterName));
    }

    private boolean isPrimitiveWrapper(final Class<?> parameterType) {
        return Integer.class == parameterType || Long.class == parameterType;
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
        if (int.class == parameterType || Integer.class == parameterType) {
            return Integer.parseInt(parameter);
        }
        if (long.class == parameterType || Long.class == parameterType) {
            return Long.parseLong(parameter);
        }
        return parameter;
    }

    private static Object getNewInstance(final Class<?> parameterType) {
        final Constructor<?>[] declaredConstructors = parameterType.getDeclaredConstructors();
        if (declaredConstructors.length == 0) {
            return null;
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
