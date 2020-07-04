package core.mvc.tobe.resolver.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArgumentResolvers {
    private static List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    static {
        resolvers.add(new RequestParameterArgumentResolver());
        resolvers.add(new JavaBeanArguementResolver());
    }

    public static HandlerMethodArgumentResolver getResolver(Class<?> parameterType) {
        return resolvers.stream()
                .filter(r -> r.isSupport(parameterType))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("resolver가 없습니다."));
    }

}
