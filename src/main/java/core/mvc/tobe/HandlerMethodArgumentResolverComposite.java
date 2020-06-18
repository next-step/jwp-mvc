package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private Set<HandlerMethodArgumentResolver> resolvers = new LinkedHashSet<>();

    public HandlerMethodArgumentResolverComposite(HandlerMethodArgumentResolver... handlerMethodArgumentResolvers) {
        this.resolvers.addAll(Arrays.asList(handlerMethodArgumentResolvers));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return getArgumentResolver(parameter).isPresent();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        Optional<HandlerMethodArgumentResolver> resolverOptional = getArgumentResolver(parameter);

        if(resolverOptional.isPresent()) {
            return resolverOptional.get().resolveArgument(parameter, request);
        }

        return null;
    }

    private Optional<HandlerMethodArgumentResolver> getArgumentResolver(MethodParameter parameter) {
        return this.resolvers.stream()
                .filter(resolver -> resolver.supportsParameter(parameter))
                .findAny();
    }
}
