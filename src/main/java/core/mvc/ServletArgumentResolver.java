package core.mvc;

import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return HttpServletRequest.class.equals(parameter.getParameterType())
                || HttpServletResponse.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        if (HttpServletRequest.class.equals(parameter.getParameterType())) {
            return request;
        }
        return response;
    }
}