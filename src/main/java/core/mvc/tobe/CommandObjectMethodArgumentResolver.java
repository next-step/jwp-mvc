package core.mvc.tobe;

import core.mvc.exception.ReflectionsException;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandObjectMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return DataTypeMethodArgumentResolver.from(methodParameter) == null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) throws Exception {
        try {
            Object newInstance = methodParameter.getType().newInstance();
            setProperty(newInstance, req);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectionsException("cannot create instance.", e);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new ReflectionsException("cannot set property on instance.", e);
        }
    }

    private void setProperty(Object newInstance, HttpServletRequest req) throws Exception {
        Class<?> clazz = newInstance.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Method setter = findSetterMethod(clazz, field);
            setter.setAccessible(true);

            Object arg = getArg(field, req);
            setter.invoke(newInstance, arg);
        }
    }

    private Method findSetterMethod(Class<?> clazz, Field field) throws NoSuchMethodException {
        String name = field.getName();
        Class<?> type = field.getType();
        return clazz.getDeclaredMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), type);
    }

    private Object getArg(Field field, HttpServletRequest req) throws Exception {
        MethodParameter fieldParameter = new MethodParameter(field.getName(), field.getType());
        DataTypeMethodArgumentResolver dataTypeMethodArgumentResolver = DataTypeMethodArgumentResolver.from(fieldParameter);
        if(dataTypeMethodArgumentResolver == null) {
            throw new RuntimeException("no matching data type.");
        }
        return dataTypeMethodArgumentResolver.resolveArgument(fieldParameter, req);
    }
}
