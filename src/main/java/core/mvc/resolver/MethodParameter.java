package core.mvc.resolver;

import com.google.common.collect.ImmutableList;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MethodParameter {

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private final Map<String, Parameter> parameters;
    private final Map<String, String> pathVariables;

    private MethodParameter(Map<String, Parameter> parameters, Map<String, String> pathVariables) {
        this.parameters = parameters;
        this.pathVariables = pathVariables;
    }

    public static MethodParameter from(Method method, String uri) {
        Map<String, Parameter> parameters = getParameters(method);
        Map<String, String> pathVariables = getPathVariables(method, uri);
        return new MethodParameter(parameters, pathVariables);
    }

    private static Map<String, Parameter> getParameters(Method method) {
        Map<String, Parameter> result = new LinkedHashMap<>();
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterNames.length; i++) {
            result.put(parameterNames[i], parameters[i]);
        }
        return result;
    }

    private static Map<String, String> getPathVariables(Method method, String uri) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return PathVariableResolver.getPathVariables(requestMapping.value(), uri);
    }

    public Class<?> getType(String parameterName) {
        return parameters.get(parameterName).getType();
    }

    public String getParameterValue(HttpServletRequest request, String parameterName) {
        if (!parameters.containsKey(parameterName)) {
            return null;
        }

        if (isPathVariable(parameterName)) {
            return pathVariables.get(parameterName);
        }

        return request.getParameter(parameterName);
    }

    private boolean isPathVariable(String parameterName) {
        Parameter parameter = parameters.get(parameterName);
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    public List<String> getParameterNames() {
        return ImmutableList.copyOf(parameters.keySet());
    }

}
