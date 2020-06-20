package core.mvc.support.resolvers;

import core.mvc.support.MethodParameter;

import javax.servlet.http.HttpServletRequest;

public class JavaBeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        return null;
    }
}
