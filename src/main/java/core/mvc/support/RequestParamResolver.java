package core.mvc.support;

import core.annotation.web.RequestParam;
import core.mvc.support.exception.MissingRequestParamException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        final String parameterName = fetchParameterName(parameter);
        final String parameterValue = request.getParameter(parameterName);

        if (parameterValue == null) {
            validateNullable(parameterName, parameter);
        }

        if (parameter.matchClass(int.class)) {
            return Integer.parseInt(parameterValue);
        }

        if (parameter.matchClass(long.class)) {
            return Long.parseLong(parameterValue);
        }

        return parameterValue;
    }

    private void validateNullable(String parameterName, MethodParameter parameter) {
        final RequestParam requestParam = (RequestParam) parameter.getAnnotation(RequestParam.class);
        final boolean isNullable = !requestParam.required();

        if (!isNullable) {
            throw new MissingRequestParamException(parameterName);
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
