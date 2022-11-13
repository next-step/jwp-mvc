package core.mvc.tobe.resolver;


import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestArgumentResolver implements ArgumentResolver{
    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(HttpServletRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  String parameterName) {
        return request;
    }
}
