package core.mvc.argumentresolver;

import core.mvc.scanner.WebApplicationScanner;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class ArgumentResolvers {

    private static final List<HandlerMethodArgumentResolver> DEFAULT_ARGUMENT_RESOLVERS = Arrays.asList(
            new HttpServletRequestArgumentResolver(),
            new HttpServletResponseArgumentResolver()
    );

    private List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    public void init(WebApplicationScanner webApplicationScanner) throws ReflectiveOperationException {
        log.debug("argumentResolvers initialize.");
        this.resolvers.addAll(DEFAULT_ARGUMENT_RESOLVERS);

        Set<Class<? extends HandlerMethodArgumentResolver>> classes = webApplicationScanner.scanClassesAssignedBy(HandlerMethodArgumentResolver.class);
        for (Class<?> clazz : classes) {
            HandlerMethodArgumentResolver instance = (HandlerMethodArgumentResolver) clazz.getDeclaredConstructor().newInstance();
            this.resolvers.add(instance);
        }
        log.debug("argumentResolvers initialization is over.");
    }

    public Object[] resolve(MethodParameter[] methodParameters, HttpServletRequest request, HttpServletResponse response) {
        Object[] parameters = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            parameters[i] = resolveArgument(methodParameters[i], request, response);
        }

        return parameters;
    }

    private Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return this.resolvers.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .map(resolver -> resolver.resolveArgument(methodParameter, request, response))
                .filter(Objects::nonNull)
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
