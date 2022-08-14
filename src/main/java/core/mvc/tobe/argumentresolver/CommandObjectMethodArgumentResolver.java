package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.exception.InvalidInstanceException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class CommandObjectMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean resolvable(final MethodParameter methodParameter) {
        final Class<?> parameterType = methodParameter.getParameterType();
        return !PrimitiveParameter.isPrimitive(parameterType) && HttpServletRequest.class != methodParameter.getParameterType();
    }

    @Override
    public Object resolve(final MethodParameter methodParameter, final HttpServletRequest request) {
        return getNewInstance(methodParameter.getParameterType(), request);
    }

    private Object getNewInstance(final Class<?> parameterType, final HttpServletRequest request) {
        final Object instance = getNewInstance(parameterType);

        final Field[] declaredFields = parameterType.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            final String parameter = request.getParameter(declaredField.getName());
            setField(instance, declaredField, parameter);
        }
        return instance;
    }

    private void setField(final Object instance, final Field declaredField, final String parameter) {
        if (Objects.isNull(parameter) || parameter.isBlank()) {
            return;
        }

        try {
            declaredField.setAccessible(true);
            declaredField.set(instance, parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getNewInstance(final Class<?> parameterType) {
        final Constructor<?>[] declaredConstructors = parameterType.getDeclaredConstructors();
        if (declaredConstructors.length == 0) {
            return null;
        }

        final Constructor<?> declaredConstructor = declaredConstructors[0];
        final Object[] constructorArguments = Arrays.stream(declaredConstructor.getParameterTypes())
            .map(clazz -> PrimitiveParameter.from(clazz).getDefaultValue())
            .toArray();

        try {
            return declaredConstructor.newInstance(constructorArguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InvalidInstanceException(e);
        }
    }
}
