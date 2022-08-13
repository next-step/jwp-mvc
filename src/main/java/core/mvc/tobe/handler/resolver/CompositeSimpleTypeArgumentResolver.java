package core.mvc.tobe.handler.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CompositeSimpleTypeArgumentResolver implements ArgumentResolver {
    private final List<AbstractSimpleTypeArgumentResolver> parameterArgumentResolvers;

    public CompositeSimpleTypeArgumentResolver(List<AbstractSimpleTypeArgumentResolver> parameterArgumentResolvers) {
        this.parameterArgumentResolvers = parameterArgumentResolvers;
    }

    @Override
    public boolean support(NamedParameter parameter) {
        return parameterArgumentResolvers.stream()
                .anyMatch(resolver -> resolver.support(parameter));
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return parameterArgumentResolvers.stream()
                .filter(resolver -> resolver.support(parameter))
                .map(resolver -> resolver.resolve(parameter, request, response))
                .findFirst()
                .orElseThrow(() -> new NoExistsArgumentResolverException("매개변수에 값을 할당할 argumentResolver가 존재하지 않습니다."));
    }
}
