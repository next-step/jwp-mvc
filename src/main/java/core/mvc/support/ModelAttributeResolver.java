package core.mvc.support;

import core.annotation.web.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ModelAttributeResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportParameter(MethodParameter parameter) {
        if (!isSupportableType(parameter)) {
            return false;
        }

        if (!parameter.isEmptyAnnotation() && !parameter.hasAnnotationType(ModelAttribute.class)) {
            return false;
        }

        return true;
    }

    private boolean isSupportableType(MethodParameter parameter) {
        final Class<?> parameterType = parameter.getType();
        return !parameterType.isPrimitive();

    }

    @Override
    public Object resolve(MethodParameter parameter, HttpServletRequest request) {
        return createParameterValue(parameter, request);
    }

    private Object createParameterValue(MethodParameter parameter, HttpServletRequest request) {
        final ModelAttribute modelAttribute = (ModelAttribute) parameter.getAnnotation(ModelAttribute.class);
        try {
            final Class<?> clazz = parameter.getType();
            final Constructor c = clazz.getDeclaredConstructor();
            c.setAccessible(true);

            final Object instance = c.newInstance();

            if (modelAttribute != null && !modelAttribute.binding()) {
                return instance;
            }

            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                final String fieldName = field.getName();
                field.setAccessible(true);

                final String parameterValueStr = request.getParameter(fieldName);
                final Object parameterValue = parseType(parameterValueStr, field);

                field.set(instance, parameterValue);
            }
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("리플렉션 예외");
        }
    }

    private Object parseType(String parameterValueStr, Field field) {
        Class<?> fieldType = field.getType();

        if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            return Integer.parseInt(parameterValueStr);
        }

        if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
            return Integer.parseInt(parameterValueStr);
        }
        return parameterValueStr;
    }

    private String fetchParameterName(MethodParameter parameter) {
        final ModelAttribute modelAttribute = (ModelAttribute) parameter.getAnnotation(ModelAttribute.class);
        if (modelAttribute != null && !"".equals(modelAttribute.value())) {
            return modelAttribute.value();
        }
        return parameter.getName();
    }
}
