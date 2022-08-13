package core.mvc.tobe.handler.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class BeanTypeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean support(NamedParameter parameter) {
        return true;
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        try {
            Class<?> type = parameter.getType();
            Constructor<?> declaredConstructor = type.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Object o = declaredConstructor.newInstance();

            Field[] declaredFields = type.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String parameterName = declaredField.getName();

                String value = request.getParameter(parameterName);
                Class<?> fieldType = declaredField.getType();

                declaredField.setAccessible(true);
                if (fieldType.isAssignableFrom(Integer.class) || fieldType.isAssignableFrom(Integer.TYPE)) {
                    declaredField.set(o, Integer.parseInt(value));
                } else if (fieldType.isAssignableFrom(Long.class) || fieldType.isAssignableFrom(Long.TYPE)) {
                    declaredField.set(o, Long.parseLong(value));
                } else if (fieldType.isAssignableFrom(String.class)) {
                    declaredField.set(o, value);
                }

            }

            return o;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
