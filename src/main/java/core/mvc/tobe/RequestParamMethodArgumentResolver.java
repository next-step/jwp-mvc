package core.mvc.tobe;

import core.annotation.web.RequestParam;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestParam.class) || BeanUtils.isSimpleProperty(parameter.getType())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(Parameter parameter, String parameterName, HttpServletRequest httpServletRequest) {
        String parameterValue = null;

        if (parameter.isAnnotationPresent(RequestParam.class)) {
            RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            parameterValue = httpServletRequest.getParameter(StringUtils.hasText(requestParam.name()) ? requestParam.name() : parameterName);
        }

        return ParameterTypeConverter.convert(parameter.getType(), Objects.isNull(parameterValue) ? httpServletRequest.getParameter(parameterName) : parameterValue);
    }
}
