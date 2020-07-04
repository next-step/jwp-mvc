package core.mvc.tobe.resolver.argument;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArgumentResolvers {
    private static List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    static {
        resolvers.add(new PathVariableArgumentResolver());
        resolvers.add(new RequestParameterArgumentResolver());
        resolvers.add(new JavaBeanArgumentResolver());
        resolvers.add(new ResponseParameterArgumentResolver());
    }

    public static HandlerMethodArgumentResolver getResolver(MethodParameter methodParameter) {
        return resolvers.stream()
                .filter(r -> r.isSupport(methodParameter))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("resolver가 없습니다."));
    }

}
