package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class HandlerMethodArgumentResolvers {
    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = List.of(new RequestParamMethodArgumentResolver(), new JavaBeanMethodArgumentResolver());

    public Object[] resolve(Method method, HttpServletRequest httpServletRequest) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        int length = parameters.length;
        Object[] arguments = new Object[length];

        for (int i = 0; i < length; i++) {
            Parameter parameter = parameters[i];
            HandlerMethodArgumentResolver handlerMethodArgumentResolver = handlerMethodArgumentResolvers.stream()
                                                                                                                 .filter(resolver -> resolver.supportsParameter(parameter))
                                                                                                                 .findFirst()
                                                                                                                 .orElseThrow(IllegalAccessError::new);

            arguments[i] = handlerMethodArgumentResolver.resolveArgument(parameter, parameterNames[i], httpServletRequest);
        }

        return arguments;
    }
}
