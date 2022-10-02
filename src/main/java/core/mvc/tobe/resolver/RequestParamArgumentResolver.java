package core.mvc.tobe.resolver;

import core.annotation.web.RequestParam;
import core.mvc.tobe.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestParamArgumentResolver implements ArgumentResolver{


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getType().equals(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String parameterName = methodParameter.getParameterName();
        Object parameter = request.getParameter(parameterName);
        return parameter;
    }
}
