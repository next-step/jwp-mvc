package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class HandlerMethodArgumentResolver {
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final ParameterInstanceCreator PARAMETER_INSTANCE_CREATOR = new ParameterInstanceCreator();

    public static Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Map<String, String> pathVariables = PathVariableResolver.getPathVariables(requestMapping.value(), request.getRequestURI());

        Object[] results = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            results[i] = resolveParameter(request, response, parameterNames[i], pathVariables, results, i, parameter, type);
        }

        return results;
    }

    private static <T> T resolveParameter(HttpServletRequest request, HttpServletResponse response, String parameterName1, Map<String, String> pathVariables, Object[] results, int i, Parameter parameter, Class<T> type) {
        if (isHttpServletRequest(type)) {
            return (T) request;
        }

        if (isHttpServletResponse(type)) {
            return (T) response;
        }

        String parameterName = parameterName1;
        if (parameter.isAnnotationPresent(PathVariable.class)) {
            return resolvePathVariable(pathVariables, parameterName, type);
        }

        return resolveRequestParameter(type, request, parameterName);
    }

    private static boolean isHttpServletResponse(Class<?> type) {
        return type == HttpServletResponse.class;
    }

    private static boolean isHttpServletRequest(Class<?> type) {
        return type == HttpServletRequest.class;
    }

    private static <T> T resolvePathVariable(Map<String, String> pathVariables, String parameterName, Class<T> type) {
        return TypeConverter.convert(type, pathVariables.get(parameterName)).orElse(null);
    }

    private static <T> T resolveRequestParameter(Class<T> type, HttpServletRequest request, String parameterName) {
        return TypeConverter.convert(type, request.getParameter(parameterName))
                .orElseGet(() -> PARAMETER_INSTANCE_CREATOR.create(type, request));
    }

}
