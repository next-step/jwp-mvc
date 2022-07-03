package core.mvc.supporter;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    public HandlerMethodArgumentResolverComposite addResolvers(HandlerMethodArgumentResolver... resolvers) {
        if (resolvers != null) {
            Collections.addAll(this.argumentResolvers, resolvers);
        }

        return this;
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return getArgumentResolver(parameter) != null;
    }

    @Override
    public Object resolveArgument(Parameter parameter, Method method, HttpServletRequest httpServletRequest, int index) throws Exception {
        HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
        if (Objects.isNull(resolver)) {
            throw new IllegalArgumentException("Unsupported parameter");
        }
        return resolver.resolveArgument(parameter, method, httpServletRequest, index);
    }

    @Nullable
    private HandlerMethodArgumentResolver getArgumentResolver(Parameter parameter) {
        return argumentResolvers.stream()
                .filter(it -> it.supportsParameter(parameter))
                .findFirst()
                .orElse(null);
    }
}
