package core.mvc.tobe.resolver;

import core.annotation.web.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
        String parameterName = getRequestParamName(requestParam, methodParameter.getParameterName());
        String value = request.getParameter(parameterName);

        Class<?> parameterType = methodParameter.getType();

        return TypeConverter.convert(parameterType, value);
    }

    private String getRequestParamName(RequestParam requestParam, String parameterName) {
        String requestParamValue = requestParam.value();
        String requestParamName = requestParam.name();
        verifyRequestParam(requestParamValue, requestParamName);
        if (requestParamValue.isEmpty() && requestParamName.isEmpty()) {
            return parameterName;
        }
        return requestParamValue.isEmpty() ? requestParamName : requestParamValue;
    }

    private void verifyRequestParam(String pathVariableValue, String pathVariableName) {
        if (!pathVariableValue.isEmpty() && !pathVariableName.isEmpty()) {
            throw new IllegalArgumentException("RequestParam 의 value, name 중 하나만 값을 할당해야 합니다.");
        }
    }
}
