package core.mvc.resolver;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver{

    private final List<HandlerMethodArgumentResolver> resolvers;

    public HandlerMethodArgumentResolverComposite(List<HandlerMethodArgumentResolver> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return resolvers.stream().anyMatch(handlerMethodArgumentResolver -> handlerMethodArgumentResolver.supportsParameter(methodParameter));
    }

    public static HandlerMethodArgumentResolverComposite newInstance() {
        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite(
            Arrays.asList(
                new HttpServletHandlerMethodArgumentResolver(),
                new PathHandlerMethodArgumentResolver(),
                new BeanHandlerMethodArgumentResolver(),
                new ParameterHandlerMethodArgumentResolver()

            )
        );
        return resolverComposite;
    }

    @Override
    public Object resolve(MethodParameter methodParameter, String mappingUrl, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        HandlerMethodArgumentResolver resolver = resolvers.stream()
            .filter(handlerMethodArgumentResolver -> handlerMethodArgumentResolver.supportsParameter(methodParameter))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException());

        return resolver.resolve(methodParameter, mappingUrl, httpRequest, httpResponse);
    }
}
