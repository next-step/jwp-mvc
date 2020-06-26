package core.mvc.support;

import core.annotation.web.PathVariable;
import core.mvc.support.exception.MissingPathPatternException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathVariableResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        return parameter.hasAnnotationType(PathVariable.class);
    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        final String requestUri = request.getRequestURI();
        final String parameterName = parameter.getName();

        final String parameterValue = parameter.getPathVariable(requestUri, parameterName)
                .orElseThrow(() -> new MissingPathPatternException(parameterName));

        if (parameter.anyMatchClass(int.class, Integer.class)) {
            return Integer.parseInt(parameterValue);
        }

        if (parameter.anyMatchClass(long.class, Long.class)) {
            return Long.parseLong(parameterValue);
        }

        return parameterValue;
    }
}
