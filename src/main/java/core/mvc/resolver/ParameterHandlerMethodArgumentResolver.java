package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;

public class ParameterHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return ParameterUtils.supportPrimitiveType(methodParameter);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, String mappingUrl, HttpServletRequest httpRequest) {
        if (!supportsParameter(methodParameter)) {
            throw new IllegalArgumentException("unSupports Parameter");
        }

        String variable = httpRequest.getParameter(methodParameter.getParameterName());
        return ParameterUtils.convertToPrimitiveType(methodParameter.toParameter(), variable);
    }
}
