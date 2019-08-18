package core.mvc.tobe.argumentresolver;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class MethodArgumentHandler {
    private Logger logger = LoggerFactory.getLogger(MethodArgumentHandler.class);
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = Lists.newArrayList();

    public void add(HandlerMethodArgumentResolver handlerMethodArgumentResolver) {
        handlerMethodArgumentResolvers.add(handlerMethodArgumentResolver);
    }

    public Object[] getValues(Method method, HttpServletRequest request) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            logger.debug("parameter : {}", parameterNames[i]);
            values[i] = getParameterValue(request, parameters[i], parameterNames[i]);
        }

        return values;
    }

    private Object getParameterValue(HttpServletRequest request, Parameter parameter, String parameterName) {
        if (CollectionUtils.isEmpty(handlerMethodArgumentResolvers)) {
            return null;
        }

        return handlerMethodArgumentResolvers.stream()
                .filter(resolver -> resolver.supportsParameter(parameter))
                .map(resolver -> resolver.resolveArgument(request, parameter, parameterName))
                .findFirst()
                .orElse(request.getParameter(parameterName));
    }
}
