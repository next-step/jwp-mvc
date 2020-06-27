package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ArgumentResolvers {
    private final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    public void add(HandlerMethodArgumentResolver resolver) {
        this.resolvers.add(resolver);
    }

    public HandlerMethodArgumentResolver getResolver(Method method) {
        return resolvers.stream()
                .filter(r -> r.support(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Resolver 를 찾을 수 없습니다."));
    }

}
