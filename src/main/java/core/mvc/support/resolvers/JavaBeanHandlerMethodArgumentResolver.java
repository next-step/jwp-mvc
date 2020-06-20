package core.mvc.support.resolvers;

import core.mvc.support.MethodParameter;
import core.mvc.utils.ReflectionUtil;
import core.mvc.utils.TypeConverter;
import core.mvc.utils.UnableToCreateInstanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;

public class JavaBeanHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(JavaBeanHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final boolean isPrimitive = methodParameter.getParameterType().isPrimitive();
        return !methodParameter.isAnnotated() && !isPrimitive;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {

        try {
            final Object argument = ReflectionUtil.instantiateClass(methodParameter.getParameterType());
            final Field[] fields = methodParameter.getParameterType().getDeclaredFields();
            injectValues(argument, fields, request);
            log.debug("instance: {}", argument);
            return argument;
        } catch (UnableToCreateInstanceException | IllegalAccessException e) {
            throw new IllegalStateException("Error occurred due to initialization failure.");
        }
    }

    private void injectValues(Object instance, Field[] fields, HttpServletRequest request) throws IllegalAccessException {
        for (Field field : fields) {
            final String fieldName = field.getName();
            final String actualValue = request.getParameter(fieldName);
            final Object value = TypeConverter.convert(actualValue, field.getType());
            field.setAccessible(true);
            field.set(instance, value);
        }
    }

}
