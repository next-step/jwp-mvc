package core.mvc.tobe.method.support;

import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.SimpleTypeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private static final List<HandlerMethodArgumentResolver> RESOLVERS = List.of(
            ServletRequestMethodArgumentResolver.getResolver(),
            new ModelAttributeMethodArgumentResolver(SimpleTypeConverter.getTypeConverter()),
            new PathVariableMethodArgumentResolver(SimpleTypeConverter.getTypeConverter()),
            RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.getTypeConverter())
    );
    private final Map<MethodParameter, HandlerMethodArgumentResolver> resolverCache = new ConcurrentHashMap<>();

    private HandlerMethodArgumentResolverComposite() {
    }

    private static class HandlerMethodArgumentResolverCompositeHolder {
        private static final HandlerMethodArgumentResolverComposite INSTANCE =
                new HandlerMethodArgumentResolverComposite();
    }

    public static HandlerMethodArgumentResolverComposite getResolverComposite() {
        return HandlerMethodArgumentResolverCompositeHolder.INSTANCE;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return RESOLVERS.stream()
                .anyMatch(resolver -> resolver.supportsParameter(parameter));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return supportedResolver(parameter).resolveArgument(parameter, request, response);
    }

    private HandlerMethodArgumentResolver supportedResolver(MethodParameter parameter) {
        return resolverCache.computeIfAbsent(parameter, this::findSupportedResolver);
    }

    private HandlerMethodArgumentResolver findSupportedResolver(MethodParameter parameter) {
        return RESOLVERS.stream()
                .filter(resolver -> resolver.supportsParameter(parameter))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("지원하지 않는 파라미터 입니다. parameter = %s", parameter)));
    }
}
