package core.mvc.tobe.resolver;

import core.mvc.tobe.resolver.method.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final HandlerMethodArgumentResolvers handlerMethodArgumentResolvers;

    public HandlerMethodArgumentResolverComposite(HandlerMethodArgumentResolvers handlerMethodArgumentResolvers) {
        this.handlerMethodArgumentResolvers = handlerMethodArgumentResolvers;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return handlerMethodArgumentResolvers.getHandlerMethodArgumentResolvers()
                .stream()
                .anyMatch(resolver -> resolver.supportsParameter(parameter));
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return handlerMethodArgumentResolvers.getHandlerMethodArgumentResolvers()
                .stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .findAny()
                .map(resolver -> resolver.resolveArgument(methodParameter, request, response))
                .orElseThrow(() -> new IllegalArgumentException("적합한 리졸버가 없습니다."));
    }
}