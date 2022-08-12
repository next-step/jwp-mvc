package core.mvc.resolver;


import core.annotation.web.RequestParam;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamArgumentResolver extends AbstractAnnotationArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String paramKey = getParamKey(methodParameter);
        Object argument = request.getParameter(paramKey);
        return resolve(methodParameter, argument);
    }

    private String getParamKey(MethodParameter methodParameter) {
        RequestParam requestParam = getAnnotation(methodParameter, RequestParam.class);
        if (StringUtils.isNotBlank(requestParam.name())) {
            return requestParam.name();
        }
        if (StringUtils.isNotBlank(requestParam.value())) {
            return requestParam.value();
        }
        return methodParameter.getParameterName();
    }
}
