package core.mvc.tobe.resolver;

import core.mvc.tobe.exception.HandlerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ArgumentResolverScanner {
    private static final List<ArgumentResolver> argumentResolvers = Arrays.asList(
            HttpRequestArgumentResolver.getInstance(),
            HttpResponseArgumentResolver.getInstance(),
            PathVariableArgumentResolver.getInstance(),
            ParameterArgumentResolver.getInstance()
    );

    private final Method method;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ArgumentResolverScanner(HttpServletRequest request, HttpServletResponse response, Method method) {
        this.request = request;
        this.response = response;
        this.method = method;
    }

    public Object[] arguments() {
        return ArgumentModels.getInstance().argumentModels(method).stream()
                .map(argumentModel -> argument(argumentResolvers, argumentModel))
                .toArray();
    }

    private Object argument(List<ArgumentResolver> argumentResolvers, ArgumentModel argumentModel) {
        return argumentResolvers.stream()
                .filter(resolver -> resolver.isSupport(argumentModel))
                .findAny()
                .orElseThrow(() -> new HandlerException("요청에 해당하는 Argument Resolver를 찾을 수 없습니다."))
                .resolve(request, response, argumentModel);
    }
}
