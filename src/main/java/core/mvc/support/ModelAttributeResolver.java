package core.mvc.support;

import core.annotation.web.ModelAttribute;
import core.mvc.support.exception.FailedNewInstanceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ModelAttributeResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        if (!isSupportableType(parameter)) {
            return false;
        }
        return true;
    }

    private boolean isSupportableType(MethodParameter parameter) {
        final Class<?> parameterType = parameter.getType();
        return !parameterType.isPrimitive();

    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return createParameterValue(parameter, request);
    }

    private Object createParameterValue(MethodParameter parameter, HttpServletRequest request) {
        final Class<?> clazz = parameter.getType();
        final Object instance = newInstance(clazz);

        final ModelAttribute modelAttribute = (ModelAttribute) parameter.getAnnotation(ModelAttribute.class);
        if (modelAttribute != null && !modelAttribute.binding()) {
            return instance;
        }

        final Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            final String fieldName = field.getName();
            final String parameterValueStr = request.getParameter(fieldName);
            final Object parameterValue = parseType(parameterValueStr, field);

            try {
                field.setAccessible(true);
                field.set(instance, parameterValue);
            } catch (IllegalAccessException e) {
                // ignore. Does not happen.
            }
        }
        return instance;
    }

    private Object newInstance(Class<?> clazz) {
        try {
            final Constructor c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FailedNewInstanceException(clazz, e);
        }
    }

    private Object parseType(String parameterValueStr, Field field) {
        final Class<?> fieldType = field.getType();

        if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            return Integer.parseInt(parameterValueStr);
        }

        if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
            return Integer.parseInt(parameterValueStr);
        }
        return parameterValueStr;
    }

}
