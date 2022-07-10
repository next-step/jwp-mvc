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
            parameterValue = getParameterValueByRequestParam(parameterName, httpServletRequest, requestParam);
        }

        return ParameterTypeConverter.convert(parameter.getType(), Objects.isNull(parameterValue) ? httpServletRequest.getParameter(parameterName) : parameterValue);
    }

    private String getParameterValueByRequestParam(String parameterName, HttpServletRequest httpServletRequest, RequestParam requestParam) {
        if (StringUtils.hasText(requestParam.value())) {
            return httpServletRequest.getParameter(requestParam.value());
        }
        if (StringUtils.hasText(requestParam.name())) {
            return httpServletRequest.getParameter(requestParam.name());
        }
        return parameterName;
    }
}
