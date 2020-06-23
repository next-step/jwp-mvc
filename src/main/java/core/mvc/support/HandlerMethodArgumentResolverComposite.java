package core.mvc.support;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerMethodArgumentResolverComposite {

    private final List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers;

    public HandlerMethodArgumentResolverComposite() {
        handlerMethodArgumentResolvers = new ArrayList<>();
    }

    public void addResolver(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        handlerMethodArgumentResolvers.add(handlerMethodArgumentResolver);
    }

    public Object[] resolveParameters(List<MethodParameter> methodParameters, HttpServletRequest request) {
        return methodParameters.stream()
                .map(p -> resolveParameter(p, request))
                .toArray();
    }

    private Object resolveParameter(MethodParameter methodParameter, HttpServletRequest request) {
        final HandlerMethodArgumentResolver resolver = findSupportableResolver(methodParameter);
        if (resolver == null) {
            return null;
        }
        return resolver.resolve(methodParameter, request);
    }

    private HandlerMethodArgumentResolver findSupportableResolver(MethodParameter methodParameter) {
        return handlerMethodArgumentResolvers.stream()
                .filter(resolver -> resolver.supportParameter(methodParameter))
                .findFirst()
                .orElse(null);
    }
}
