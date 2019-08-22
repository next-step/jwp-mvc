package core.mvc;

import core.di.factory.MethodParameter;

import javax.servlet.http.HttpServletRequest;

public class ParamNameArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return StringConverters.getInstance().supports(methodParameter.getType());
    }

    @Override
    public Object resolve(MethodParameter methodParameter, WebRequest webRequest) {
        Class<?> type = methodParameter.getType();
        HttpServletRequest request = webRequest.getRequest();
        String parameterName = methodParameter.getName();

         return StringConverters.getInstance()
                .getConverter(type)
                .convert(request.getParameter(parameterName));
    }
}
