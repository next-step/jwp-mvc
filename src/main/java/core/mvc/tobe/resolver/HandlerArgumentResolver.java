package core.mvc.tobe.resolver;

import com.google.common.collect.Lists;
import core.mvc.tobe.util.PathVariableUtil;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
public class HandlerArgumentResolver {
    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private static final List<HandlerMethodArgumentResolver> handlerArgumentResolver = Arrays.asList(
        new PathVariableArgumentResolver(),
        new HttpServletRequestArgumentResolver(),
        new HttpServletResponseArgumentResolver(),
        new PrimitiveArgumentResolver(),
        new CustomArgumentResolver()
    );

    public static Object[] resolveParameters(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Map<String, String> pathVariables = PathVariableUtil.getPathVariables(method, request.getRequestURI());
        log.debug("pathVariables: {}", StringUtils.toPrettyJson(pathVariables));

        List<Object> arguments = Lists.newArrayList();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            String parameterName = parameterNames[i];

            MethodParameter methodParameter = MethodParameter.from(parameterName, parameter, method, pathVariables);
            Object parameterValue = resolveParameters(methodParameter, request, response);
            arguments.add(parameterValue);

            log.debug("parameterType: {}, parameterName: {}, parameterValue: {}", parameterName, type, parameterValue);
        }

        return arguments.toArray(new Object[0]);
    }

    private static Object resolveParameters(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        return handlerArgumentResolver.stream()
            .filter(resolver -> resolver.supportsParameter(methodParameter))
            .peek(resolver -> log.debug("found resolver: {}", resolver.getClass()))
            .map(resolver -> resolver.resolveArgument(methodParameter, request, response))
            .findFirst()
            .orElse(null);
    }
}
