package core.mvc.resolver;

import core.annotation.Component;
import org.reflections.Reflections;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerMethodArgumentResolverMapper {

    private final String RESOLVER_COMPONENT_SCAN_BASE = "core.mvc.resolver";
    private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public void initialize() { // TODO(ComponentScan): ComponentScan, Configuration 이용하여 리팩토링
        Reflections reflections = new Reflections(RESOLVER_COMPONENT_SCAN_BASE);
        reflections.getTypesAnnotatedWith(Component.class).stream()
                .filter(clazz -> HandlerMethodArgumentResolver.class.isAssignableFrom(clazz))
                .forEach(clazz -> {
                    try {
                        argumentResolvers.add((HandlerMethodArgumentResolver) clazz.getConstructor().newInstance());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("fail to initialize argument resolver");
                    }
                });
    }

    public Object[] resolve(Method method, HttpServletRequest httpServletRequest) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        int parametersNum = parameterNames.length;
        Object[] values = new Object[parametersNum];

        for (int i = 0; i < parametersNum; i++) {
            MethodParameter methodParameter = new MethodParameter(method, parameters[i]);
            values[i] = getArgumentResolver(methodParameter).resolveArgument(methodParameter, httpServletRequest);
        }

        return values;
    }

    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter methodParameter) {
        return argumentResolvers.stream()
                .filter(resolver -> resolver.supportsParameter(methodParameter))
                .findFirst()
                .orElseThrow(()-> new NoSuchElementException(methodParameter.toString()));
    }
}
