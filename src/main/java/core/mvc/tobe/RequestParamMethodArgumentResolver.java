package core.mvc.tobe;

import core.annotation.web.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamMethodArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> type = parameter.getType();
        String name = parameter.getParameterName();
        String value = request.getParameter(name);

        if (parameter.isStringType()) {
            return value;
        }

        TypeConverter converter = TypeConverter.valueOf(type);
        return converter.convert(value);
    }
}
