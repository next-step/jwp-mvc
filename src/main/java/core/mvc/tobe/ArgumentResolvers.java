package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ArgumentResolvers {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Class clazz;
    private Method method;

    public ArgumentResolvers(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public Object[] getParameterValues(final HttpServletRequest request) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        Object[] values = new Object[parameterNames.length];

        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];

            final Class<?>[] parameterTypes = method.getParameterTypes();
            for (final Class<?> parameterType : parameterTypes) {
                values[i] = new BasicTypeArgumentResolver(parameterType).getParameterValue(request.getParameter(parameterName));
            }
        }
        return values;
    }


    private boolean isPathVariable() {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(annotation -> annotation instanceof PathVariable);
    }

    private boolean isDefaultClass() {
        return clazz.isInstance(String.class) || clazz.isPrimitive();
    }

    private boolean isInterface() {
        return Arrays.stream(method.getParameterTypes())
                .anyMatch(Class::isInterface);
    }

}
