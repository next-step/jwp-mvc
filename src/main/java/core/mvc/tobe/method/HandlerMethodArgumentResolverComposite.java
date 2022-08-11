package core.mvc.tobe.method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private static final HandlerMethodArgumentResolverComposite INSTANCE = new HandlerMethodArgumentResolverComposite();
    private static final List<HandlerMethodArgumentResolver> DEFAULT_RESOLVERS = List.of(
            ServletWebMethodArgumentResolver.instance(),
            PathVariableMethodArgumentResolver.from(SimpleTypeConverter.instance()),
            RequestParamMethodArgumentResolver.of(true, SimpleTypeConverter.instance()),
            ModelAttributeMethodArgumentResolver.from(SimpleTypeConverter.instance())
    );

    private final Map<MethodParameter, HandlerMethodArgumentResolver> resolverCache = new HashMap<>();

    private HandlerMethodArgumentResolverComposite() {
        if (INSTANCE != null) {
            throw new AssertionError(String.format("%s can not be instanced", getClass()));
        }
    }

    public static HandlerMethodArgumentResolverComposite instance() {
        return INSTANCE;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DEFAULT_RESOLVERS.stream()
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
        return DEFAULT_RESOLVERS.stream()
                .filter(resolver -> resolver.supportsParameter(parameter))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("unsupported parameter(%s). supportsParameter should be called first", parameter)));
    }
}
