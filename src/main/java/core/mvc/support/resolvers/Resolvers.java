package core.mvc.support.resolvers;

import core.mvc.support.MethodParameter;
import core.mvc.support.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Resolvers {

    private final static List<HandlerMethodArgumentResolver> RESOLVERS = new ArrayList<>();
    static {
        RESOLVERS.add(new PathVariableMethodArgumentResolver());
        RESOLVERS.add(new PrimitiveTypeHandlerMethodArgumentResolver());
        RESOLVERS.add(new JavaBeanHandlerMethodArgumentResolver());
        RESOLVERS.add(new CookieValueHandlerMethodArgumentResolver());
    }

    public static Object[] resolveArguments(MethodSignature methodSignature, HttpServletRequest request) {
        return methodSignature.getMethodParameters()
                .stream()
                .map(methodParameter -> resolve(methodParameter, request)).toArray();
    }

    private static Object resolve(MethodParameter methodParameter, HttpServletRequest request) {
        final Optional<HandlerMethodArgumentResolver> maybeResolver = getResolver(methodParameter);
        if (maybeResolver.isPresent()) {
            final HandlerMethodArgumentResolver resolver = maybeResolver.get();
            return resolver.resolveArgument(methodParameter, request);
        }
        return null;
    }

    private static Optional<HandlerMethodArgumentResolver> getResolver(MethodParameter methodParameter) {
        return RESOLVERS.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .findFirst();
    }
}
