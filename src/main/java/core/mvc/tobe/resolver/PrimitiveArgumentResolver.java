package core.mvc.tobe.resolver;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class PrimitiveArgumentResolver extends AbstractHandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return isPrimitiveOrWrapperType(parameter.getType()) || String.class.equals(parameter.getType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        String foundParameter = request.getParameter(parameter.getName());
        log.debug("foundParameter: {}", foundParameter);
        return resolveArgument(foundParameter, parameter.getType());
    }
}
