package core.mvc.tobe.resolver.argument;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArgumentResolvers {
    private static List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    static {
        resolvers.add(new PathVariableArgumentResolver());
        resolvers.add(new RequestParameterArgumentResolver());
        resolvers.add(new JavaBeanArguementResolver());
    }

    public static HandlerMethodArgumentResolver getResolver(Class<?> parameterType, Parameter parameter) {
        return resolvers.stream()
                .filter(r -> r.isSupport(parameterType, parameter))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("resolver가 없습니다."));
    }

}
