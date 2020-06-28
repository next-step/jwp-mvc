package core.mvc.tobe;

import core.annotation.web.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

import static core.mvc.tobe.ParameterUtils.decideParameter;

public class RequestParameterArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(RequestParameterArgumentResolver.class);
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean support(Method method) {
        return Arrays.stream(method.getParameters())
                .flatMap(p -> Arrays.stream(p.getAnnotations()))
                .noneMatch(a -> a.annotationType().equals(PathVariable.class));
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
