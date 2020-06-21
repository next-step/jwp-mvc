package core.mvc;

import core.annotation.web.PathVariable;
import core.exception.CoreException;
import core.exception.CoreExceptionStatus;
import lombok.Getter;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Getter
public class MethodParameters {
    Map<String, Parameter> parameters = new LinkedHashMap<>();

    public MethodParameters(Method method) {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Parameter[] methodParameters = method.getParameters();

        for (int i = 0; i < parameterNames.length; i++) {
            parameters.put(parameterNames[i], methodParameters[i]);
        }
    }

    public Object[] getArgs(HttpServletRequest request, HttpServletResponse response, RequestParameters requestParameters, PathVariables pathVariables) {
        List<Object> args = new ArrayList<>();

        for (Map.Entry<String, Parameter> parameterEntry : parameters.entrySet()) {
            String name = parameterEntry.getKey();
            Parameter parameter = parameterEntry.getValue();

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                Object arg = getPathVariableObject(parameter, name, pathVariables);
                args.add(arg);
                continue;
            }

            if (parameter.getType().equals(HttpServletRequest.class)) {
                args.add(request);
                continue;
            }

            if (parameter.getType().equals(HttpServletResponse.class)) {
                args.add(response);
                continue;
            }

            Object requestParameter = requestParameters.getParameter(name);
            if (Objects.isNull(requestParameter)) {
                args.add(requestParameters.getBodyObject(parameter.getType()));
                continue;
            }

            args.add(requestParameter);
        }

        return args.toArray();
    }

    private Object getPathVariableObject(Parameter parameter, String parameterName, PathVariables pathVariables) {
        PathVariable requestMapping = parameter.getAnnotation(PathVariable.class);
        if (!requestMapping.value().isEmpty()) {
            parameterName = requestMapping.value();
        }

        Object arg = pathVariables.get(parameterName, parameter.getType());
        if (requestMapping.required() && Objects.isNull(arg)) {
            throw new CoreException(CoreExceptionStatus.INVALID_PATH_VARIABLE);
        }

        return arg;
    }
}
