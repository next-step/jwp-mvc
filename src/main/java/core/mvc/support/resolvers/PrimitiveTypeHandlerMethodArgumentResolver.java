package core.mvc.support.resolvers;

import core.mvc.support.MethodParameter;

import javax.servlet.http.HttpServletRequest;

/**
 * Resolver for primitive type without any annotation.
 *
 * @author hyeyoom
 */
public class PrimitiveTypeHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return !methodParameter.isAnnotated();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        return null;
    }
}
