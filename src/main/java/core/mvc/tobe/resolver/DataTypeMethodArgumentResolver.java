package core.mvc.tobe.resolver;

import core.mvc.exception.ReflectionsException;
import core.mvc.tobe.MethodParameter;
import core.mvc.utils.DataParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

public class DataTypeMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        return !methodParameter.hasAnnotation();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (DataParser.supports(methodParameter.getType())) {
            return getArg(methodParameter.getName(), methodParameter.getType(), req);
        }
        return resolveCommandObject(methodParameter, req);
    }

    private Object resolveCommandObject(MethodParameter methodParameter, HttpServletRequest req) throws Exception {
        try {
            Object newInstance = methodParameter.getType().newInstance();
            setProperty(newInstance, req);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectionsException("cannot create instance.", e);
        }
    }

    private void setProperty(Object newInstance, HttpServletRequest req) throws IllegalAccessException {
        Class<?> clazz = newInstance.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            field.set(newInstance, getArg(field.getName(), field.getType(), req));
        }
    }

    private Object getArg(String name, Class<?> type, HttpServletRequest req) {
        DataParser dataParser = DataParser.from(type);
        return dataParser.parse(req.getParameter(name));
    }
}
