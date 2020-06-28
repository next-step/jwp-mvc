package core.mvc.tobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static core.mvc.tobe.ParameterUtils.decideParameter;
import static core.mvc.tobe.ParameterUtils.isPrimitive;
import static core.mvc.tobe.ParameterUtils.isString;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(RequestParameterArgumentResolver.class);
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean support(Method method) {
        return isPrimitive(method) || isString(method);
    }

    @Override
    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            Class<?> parameterType = parameterTypes[i];
            logger.debug("parameter : {}", parameterName);
            values[i] = decideParameter(request.getParameter(parameterName), parameterType);
        }
        return values;
    }

}
