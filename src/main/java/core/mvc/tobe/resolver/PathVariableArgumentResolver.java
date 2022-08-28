package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        RequestMapping requestMapping = methodParameter.getMethodAnnotation(RequestMapping.class);
        String requestPath = requestMapping.value();

        PathVariable pathVariable = methodParameter.getParameterAnnotation(PathVariable.class);
        String pathVariableName = getPathVariableName(pathVariable, methodParameter.getParameterName());
        Map<String, String> uriVariables = PathPatternUtils.getUriVariables(requestPath, request.getRequestURI());
        return TypeConverter.convert(methodParameter.getType(), uriVariables.get(pathVariableName));
    }

    private String getPathVariableName(PathVariable pathVariable, String parameterName) {
        String pathVariableValue = pathVariable.value();
        String pathVariableName = pathVariable.name();
        verifyPathVariable(pathVariableValue, pathVariableName);
        if (pathVariableValue.isEmpty() && pathVariableName.isEmpty()) {
            return parameterName;
        }
        return pathVariableValue.isEmpty() ? pathVariableName : pathVariableValue;
    }

    private void verifyPathVariable(String pathVariableValue, String pathVariableName) {
        if (!pathVariableValue.isEmpty() && !pathVariableName.isEmpty() && pathVariableName.equals(pathVariableValue)) {
            throw new IllegalArgumentException(String.format("PathVariable 의 value, name 둘다 할당 했다면, 둘의 값은 같아야 합니다. 현재 value : %s, 현재 name : %s", pathVariableValue, pathVariableName));
        }
    }
}
