package core.resolver;

import core.di.factory.MethodParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerMethodArgumentResolvers {

    private final List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();

    private Optional<HandlerMethodArgumentResolver> defaultResolver = Optional.empty();
    
    private HandlerMethodArgumentResolvers() {
    }

    public static HandlerMethodArgumentResolvers getInstance() {
        return Lazy.INSTANCE;
    }

    public HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) {
        Optional<HandlerMethodArgumentResolver> firstResolver = handlerMethodArgumentResolvers.stream()
                .filter(resolver -> resolver.supports(methodParameter))
                .findFirst();
        
        if(firstResolver.isPresent()) {
        	return firstResolver.get();
        }
        
        if(this.defaultResolver.isPresent()) {
        	return this.defaultResolver.get();
        }
        
        throw new RuntimeException("Cannot findResolver");
    }

    public HandlerMethodArgumentResolvers add(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        this.handlerMethodArgumentResolvers.add(handlerMethodArgumentResolver);
        return this;
    }

    public void setDefaultResolver(HandlerMethodArgumentResolver defaultResolver) {
    	this.defaultResolver = Optional.ofNullable(defaultResolver);
    }

    private static class Lazy {
        public static final HandlerMethodArgumentResolvers INSTANCE = new HandlerMethodArgumentResolvers();
    }
}
