package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParameterHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return ParameterUtils.supportPrimitiveType(methodParameter);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, String mappingUrl, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (!supportsParameter(methodParameter)) {
            throw new IllegalArgumentException("unSupports Parameter");
        }

        String variable = httpRequest.getParameter(methodParameter.getParameterName());
        return ParameterUtils.convertToPrimitiveType(methodParameter.toParameter(), variable);
    }
}
