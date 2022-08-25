package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.argumentResolver.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

public class RequestParameters {

    private static final List<HandlerMethodArgumentResolver> resolvers = Arrays.asList(new HttpServletRequestArgumentResolver(),
            new HttpServletResponseArgumentResolver(), new TestUserArgumentResolver(), new StringArgumentResolver(),
            new IntegerArgumentResolver(), new LongArgumentResolver());

    private final String[] parameterNames;
    private Class<?>[] parameterTypes;
    private Map<String, String> pathVariables;
    private List<Object> parameterList;

    public RequestParameters(HttpServletRequest request, Method method) {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        parameterNames = nameDiscoverer.getParameterNames(method);
        if (parameterNames == null) {
            return;
        }
        pathVariables = pathVariables(request, method);
        parameterTypes = method.getParameterTypes();
        parameterList = new ArrayList<>();
    }

    public List<Object> extraction(HttpServletRequest request, HttpServletResponse response) {
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterType = parameterTypes[i];

            for (HandlerMethodArgumentResolver resolver : resolvers) {
                if (resolver.supportsParameter(parameterType)) {
                    add(resolver.resolveArgument(parameterType, arguments(request, response, parameterName)), parameterType);
                }
            }
        }
        return parameterList;
    }

    private Map<String, String> pathVariables(HttpServletRequest request, Method method) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        UriPathPattern uriPathPattern = new UriPathPattern(requestMappingAnnotation.value());
        return uriPathPattern.matchAndExtract(request.getRequestURI());
    }

    private List<Object> arguments(HttpServletRequest request, HttpServletResponse response, String parameterName) {
        List<Object> arguments = new ArrayList<>();
        arguments.add(request.getParameter(parameterName));
        arguments.add(pathVariables.keySet().stream().filter(key -> key.equals(parameterName)).map(pathVariables::get).findFirst().orElse(null));
        arguments.add(request.getAttribute(parameterName));
        arguments.add(request);
        arguments.add(response);
        return arguments;
    }

    private void add(Object object, Class<?> parameterType) {
        parameterList.add(ParameterTypeEnum.casting(object, parameterType));
    }
}
