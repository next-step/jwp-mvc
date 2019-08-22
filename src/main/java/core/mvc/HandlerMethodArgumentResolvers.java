package core.mvc;

import core.di.factory.MethodParameter;

import java.util.ArrayList;
import java.util.List;

public class HandlerMethodArgumentResolvers {

    private final List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();

    private HandlerMethodArgumentResolvers() {
    }

    public static HandlerMethodArgumentResolvers getInstance() {
        return Lazy.INSTANCE;
    }

    public HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) {
        return handlerMethodArgumentResolvers.stream()
                .filter(resolver -> resolver.supports(methodParameter))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot findResolver"));
    }

    public HandlerMethodArgumentResolvers add(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        this.handlerMethodArgumentResolvers.add(handlerMethodArgumentResolver);
        return this;
    }


    private static class Lazy {
        public static final HandlerMethodArgumentResolvers INSTANCE = new HandlerMethodArgumentResolvers();
    }
}
