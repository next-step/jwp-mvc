package core.mvc.tobe.argumentresolver.util;

import core.mvc.tobe.argumentresolver.MethodParameter;
import core.mvc.tobe.argumentresolver.exception.TargetBeanCreateException;
import core.mvc.tobe.argumentresolver.exception.TargetBeanFieldSettingException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class TargetBean {
    private static final int NO_ARGS = 0;

    public static Object createWithNoArgs(MethodParameter methodParameter) {
        Constructor<?>[] constructors = methodParameter.getType().getConstructors();
        try {
            return getDefaultConstructor(constructors).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new TargetBeanCreateException();
        }
    }

    public static void setFields(Object targetBean, HttpServletRequest request, MethodParameter methodParameter) {
        Field[] declaredFields = getDeclaredFields(methodParameter);

        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            declaredField.setAccessible(true);
            setField(targetBean, declaredField, request);
        }
    }

    private static void setField(Object targetBean, Field declaredField, HttpServletRequest request) {
        String parameter = request.getParameter(declaredField.getName());
        Class<?> type = declaredField.getType();

        try {
            declaredField.set(targetBean, PrimitiveParameterUtil.parseWithType(parameter, type));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new TargetBeanFieldSettingException();
        }
    }

    private static Field[] getDeclaredFields(MethodParameter methodParameter) {
        return methodParameter.getType().getDeclaredFields();
    }

    private static Constructor getDefaultConstructor(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(TargetBean::isDefaultConstructor)
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }

    private static boolean isDefaultConstructor(Constructor<?> constructor) {
        int parameters = getParamCount(constructor);
        return parameters == NO_ARGS;
    }

    private static int getParamCount(Constructor<?> constructor) {
        return constructor.getParameters().length;
    }
}
