package core.mvc.resolver;

import core.annotation.Component;
import org.springframework.beans.BeanUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
public class ModelAttributeArgumentResolver implements HandlerMethodArgumentResolver {

    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !BeanUtils.isSimpleValueType(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        try {
            return resolveObjectArgument(parameter, request);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object resolveObjectArgument(MethodParameter parameter, HttpServletRequest request) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> type = parameter.getParameterType();
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            return createConstructorArgumentInstance(request, constructor);
        }
        throw new IllegalStateException("fail to create instance");
    }

    private Object createConstructorArgumentInstance(HttpServletRequest request, Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < arguments.length; i++) {
            String value = request.getParameter(parameterNames[i]);
            arguments[i] = convertValue(parameterTypes[i], value);
        }
        return constructor.newInstance(arguments);
    }

    private static Object convertValue(Class<?> type, String value) {
        if (type == String.class) {
            return value;
        }
        return PrimitiveType.convert(type, value);
    }
}
