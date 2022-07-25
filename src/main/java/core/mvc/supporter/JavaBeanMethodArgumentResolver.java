package core.mvc.supporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class JavaBeanMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        Class<?> type = parameter.getType();
        return !type.isPrimitive() && !String.class.equals(type) && !HttpSession.class.equals(type);
    }

    @Override
    public Object resolveArgument(Parameter parameter, Method method, HttpServletRequest httpServletRequest, int index) throws Exception {
        final Object argument = parameter.getType().newInstance();
        Field[] fields = parameter.getType().getDeclaredFields();

        Arrays.stream(fields).forEach(field -> {
                    try {
                        final String fieldName = field.getName();
                        final String value = httpServletRequest.getParameter(fieldName);

                        Object result = TypeConverter.convert(parameter.getType(), value);

                        field.setAccessible(true);
                        field.set(argument, result);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        return argument;
    }
}
