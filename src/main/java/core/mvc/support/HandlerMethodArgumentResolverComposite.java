package core.mvc.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerMethodArgumentResolverComposite {

    private final List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();

    public HandlerMethodArgumentResolverComposite() {
    }

    public void addResolver(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        handlerMethodArgumentResolvers.add(handlerMethodArgumentResolver);
    }

    public Object[] resolveParameters(List<MethodParameter> methodParameters, HttpServletRequest request, HttpServletResponse response) {
        return methodParameters.stream()
                .map(p -> resolveParameter(p, request, response))
                .toArray();
    }

    private Object resolveParameter(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        final HandlerMethodArgumentResolver resolver = findSupportableResolver(methodParameter);
        if (resolver == null) {
            return null;
        }
        return resolver.resolve(methodParameter, request, response);
    }

    private HandlerMethodArgumentResolver findSupportableResolver(MethodParameter methodParameter) {
        return handlerMethodArgumentResolvers.stream()
                .filter(resolver -> resolver.supportParameter(methodParameter))
                .findFirst()
                .orElse(null);
    }
}
