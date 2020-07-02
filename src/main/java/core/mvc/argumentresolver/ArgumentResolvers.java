package core.mvc.argumentresolver;

import core.annotation.web.PathVariable;
import core.mvc.scanner.WebApplicationScanner;
import core.mvc.tobe.HandlerMethod;
import core.mvc.utils.UriPathPatternParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class ArgumentResolvers {

    private static final Class<PathVariable> PATH_VARIABLE_ANNOTATION = PathVariable.class;
    private static final List<HandlerMethodArgumentResolver> DEFAULT_ARGUMENT_RESOLVERS = Arrays.asList(
            new HttpServletRequestArgumentResolver(),
            new HttpServletResponseArgumentResolver()
    );

    private final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final PathPatternParser pathPatternParser = new PathPatternParser();

    public void init(WebApplicationScanner webApplicationScanner) throws ReflectiveOperationException {
        log.debug("argumentResolvers initialize.");
        this.resolvers.addAll(DEFAULT_ARGUMENT_RESOLVERS);

        Set<Class<? extends HandlerMethodArgumentResolver>> classes = webApplicationScanner.scanClassesAssignedBy(HandlerMethodArgumentResolver.class);
        for (Class<?> clazz : classes) {
            HandlerMethodArgumentResolver instance = (HandlerMethodArgumentResolver) clazz.getDeclaredConstructor().newInstance();
            this.resolvers.add(instance);
        }
        log.debug("argumentResolvers initialization is over.");

        this.pathPatternParser.setMatchOptionalTrailingSeparator(true);
    }

    public Object[] resolve(HandlerMethod handlerMethod, HttpServletRequest request, HttpServletResponse response) {
        MethodParameter[] methodParameters = getMethodParametersFrom(handlerMethod);

        Object[] parameters = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            parameters[i] = resolveArgument(methodParameters[i], request, response);
        }

        return parameters;
    }

    private MethodParameter[] getMethodParametersFrom(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();

        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        MethodParameter[] methodParameters = new MethodParameter[parameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            methodParameters[i] = MethodParameter.builder()
                    .parameter(parameters[i])
                    .parameterName(parameterNames[i])
                    .requestMappingUri(handlerMethod.getRequestMappingUri())
                    .build();
        }

        return methodParameters;
    }

    private Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        if (methodParameter.hasAnnotation(PATH_VARIABLE_ANNOTATION)) {
            return resolvePathVariableArgument(methodParameter, request);
        }

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

    private Object resolvePathVariableArgument(MethodParameter methodParameter, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (UriPathPatternParser.mismatch(methodParameter.getRequestMappingUri(), requestUri)) {
            throw new IllegalArgumentException("Path variable mismatch with request uri.");
        }

        Map<String, String> uriVariables = UriPathPatternParser.getUriVariables(methodParameter.getRequestMappingUri(), requestUri);
        String argument = uriVariables.get(methodParameter.getParameterName());

        return convertTypeOfArgument(argument, methodParameter.getParameterType());
    }

    private Object convertTypeOfArgument(String argument, Class<?> parameterType) {
        if (parameterType.equals(int.class)) {
            return Integer.parseInt(argument);
        }
        if (parameterType.equals(long.class)) {
            return Long.parseLong(argument);
        }
        return argument;
    }
}
