package core.mvc.resolver;

import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 *  @RequestBody 도 처리가능(단, RequestBody에 Primitive Type이 들어오면 PrimitiveTypeArgumentResolver 에서 처리함)
  */
public class UserObjectTypeArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();

        return (isSingleConstructor(parameterType) && isAllSimpleTypeInConstructorParameters(parameterType));
    }

    private boolean isSingleConstructor(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();
        return (declaredConstructors.length == 1);
    }

    /*
        Simple 타입은 Wrapper 또는 Primitive Type 인 경우를 의미한다.
     */
    private boolean isAllSimpleTypeInConstructorParameters(Class<?> methodParameter) {
        Constructor<?>[] declaredConstructors = methodParameter.getDeclaredConstructors();

        Constructor<?> declaredConstructor = declaredConstructors[0];
        Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();

        return Arrays.stream(parameterTypes)
                .allMatch(type -> isSimpleAndStringType(type));
    }

    private boolean isSimpleAndStringType(Class<?> parameterType) {
        return ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType.equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParam, HttpServletRequest request, HttpServletResponse response) {
        Parameter parameter = methodParam.getParameter();
        Class<?> type = parameter.getType();
        Field[] declaredFields = type.getDeclaredFields();

        return getObject(request, type, declaredFields);
    }

    private Object getObject(HttpServletRequest request, Class<?> type, Field[] declaredFields) {
        Object[] objects = new Object[declaredFields.length];
        int i = 0;
        for (Field field : declaredFields) {
            objects[i++] = request.getParameter(field.getName());
        }

        return getObjectByConstructor(type, objects);
    }

    private Object getObjectByConstructor(Class<?> type, Object[] objects) {
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
