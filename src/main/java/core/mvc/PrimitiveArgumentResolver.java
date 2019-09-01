package core.mvc;

import core.annotation.web.PathVariable;
import core.mvc.support.PathVariableUtils;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrimitiveArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (!parameter.hasAnnotation(PathVariable.class) && parameter.getParameterType().isPrimitive())
                || parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> type = parameter.getParameterType();
        String arg = request.getParameter(parameter.getParameterName());
        return PathVariableUtils.toPrimitiveValue(type, arg);
    }
}