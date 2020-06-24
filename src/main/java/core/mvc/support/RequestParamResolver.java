package core.mvc.support;

import core.annotation.web.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class RequestParamResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        if (!isSupportableType(parameter)) {
            return false;
        }

        if (!parameter.isEmptyAnnotation() && !parameter.hasAnnotationType(RequestParam.class)) {
            return false;
        }

        return true;
    }

    private boolean isSupportableType(MethodParameter parameter) {
        return parameter.anyMatchClass(String.class, int.class, long.class);
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request) {
        final String parameterName = fetchParameterName(parameter);
        final Object parameterValue = request.getParameter(parameterName);

        if (parameterValue == null) {
            validateNullable(parameterName, parameter);
        }

        if (parameter.matchClass(int.class)) {
            return Integer.valueOf((int) parameterValue);
        }

        if (parameter.matchClass(long.class)) {
            return Long.valueOf((long) parameterValue);
        }

        return parameterValue;
    }

    private void validateNullable(String parameterName, MethodParameter parameter) {
        final RequestParam requestParam = (RequestParam) parameter.getAnnotation(RequestParam.class);
        final boolean isNullable = !requestParam.required();

        if (!isNullable) {
            throw new RuntimeException("파라미터(" + parameterName + ")의 값이 비어있습니다.");
        }
    }

    private String fetchParameterName(MethodParameter parameter) {
        final RequestParam requestParam = (RequestParam) parameter.getAnnotation(RequestParam.class);
        if (requestParam != null && !"".equals(requestParam.value())) {
            return requestParam.value();
        }
        return parameter.getName();
    }
}
