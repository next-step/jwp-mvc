package core.mvc.tobe.resolver;

import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrimitiveArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.isPrimitiveOrWrapperType(parameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return resolveFromRequestParams(request.getParameterMap(), parameter.getName(), parameter.getType());
    }
}
