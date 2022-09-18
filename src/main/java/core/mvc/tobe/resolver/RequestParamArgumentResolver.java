package core.mvc.tobe.resolver;


import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  String parameterName) {
        RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);



        String key = StringUtils.isNotBlank(requestParam.name()) ? requestParam.name() : parameterName;
        String result = request.getParameter(key);

        if (StringUtils.isEmpty(result) && requestParam.required()) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
