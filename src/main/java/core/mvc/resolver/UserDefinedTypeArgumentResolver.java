package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class UserDefinedTypeArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();

        return (isSingleConstructor(parameterType) && isAllPrimitiveTypeInConstructorParameters(parameterType));
    }

    private boolean isSingleConstructor(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();
        return (declaredConstructors.length == 1);
    }

    private boolean isAllPrimitiveTypeInConstructorParameters(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();

        Constructor<?> declaredConstructor = declaredConstructors[0];
        Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();

        return Arrays.stream(parameterTypes)
                .allMatch(type -> isSimpleType(type));
    }

    private boolean isSimpleType(Class<?> parameterType) {
        return parameterType.isPrimitive() ||
                Number.class.isAssignableFrom(parameterType) ||
                String.class.isAssignableFrom(parameterType) ||
                Boolean.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParam, HttpServletRequest request) {
        Parameter parameter = methodParam.getParameter();
        Class<?> type = parameter.getType();
        Field[] declaredFields = type.getDeclaredFields();

        Object[] objects = new Object[declaredFields.length];
        int i = 0;
        for (Field field : declaredFields) {
            objects[i++] = request.getParameter(field.getName());
        }

        Object o = null;
        try {
            Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();
            Constructor<?> declaredConstructor = declaredConstructors[0];
            o = declaredConstructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }
}
