package core.mvc.argumentresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentResolvers {

    private List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    public void init() {
        this.resolvers.addAll(Arrays.asList(
                new HttpServletRequestArgumentResolver(),
                new HttpServletResponseArgumentResolver()
        ));
    }

    public Object[] resolve(MethodParameter[] methodParameters, HttpServletRequest request, HttpServletResponse response) {
        Object[] parameters = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            parameters[i] = resolveArgument(methodParameters[i], request, response);
        }

        return parameters;
    }

    private Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return resolvers.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .map(resolver -> resolver.resolveArgument(methodParameter, request, response))
                .findFirst()
                .orElseThrow(
                        () -> {
                            String errorMessage = String.format("cannot resolve argument : type=%s, name=%s",
                                    methodParameter.getParameterType(),
                                    methodParameter.getParameterName());
                            return new IllegalArgumentException(errorMessage);
                        }
                );
    }
}
