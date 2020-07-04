package core.mvc.tobe.argumentresolver.util;

import core.mvc.tobe.argumentresolver.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class BeanManager {
    private static final int NO_ARGS = 0;

    public static Object createBeanWithNoArgs(MethodParameter methodParameter) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructors = methodParameter.getType().getConstructors();
        return getDefaultConstructor(constructors).newInstance();
    }

    public static Object setFields(Field[] declaredFields, HttpServletRequest request, Object targetBean) throws IllegalAccessException {
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            declaredField.setAccessible(true);

            String parameter = request.getParameter(declaredField.getName());
            Class<?> type = declaredField.getType();

            declaredField.set(targetBean, ParameterUtil.parseWithType(parameter, type));
        }
        return targetBean;
    }

    private static Constructor getDefaultConstructor(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(BeanManager::isDefaultConstructor)
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
