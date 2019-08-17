package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class HandlerMethodArgumentResolver {
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final ParameterInstanceCreator PARAMETER_INSTANCE_CREATOR = new ParameterInstanceCreator();

    public static Object[] resolve(Method method, HttpServletRequest request) {
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        Parameter[] parameters = method.getParameters();

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Map<String, String> pathVariables = PathVariableResolver.getPathVariables(requestMapping.value(), request.getRequestURI());

        Object[] results = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameterNames[i];
            Class<?> type = parameter.getType();

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                results[i] = TypeConverter.convert(type, pathVariables.get(parameterName)).orElse(null);
                continue;
            }
            results[i] = convert(type, request, parameterName);
        }

        return results;
    }

    private static <T> T convert(Class<T> type, HttpServletRequest request, String parameterName) {
        return TypeConverter.convert(type, request.getParameter(parameterName))
                .orElseGet(() -> PARAMETER_INSTANCE_CREATOR.create(type, request));
    }

}
