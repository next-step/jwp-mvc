package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static core.mvc.tobe.ParameterUtils.decideParameter;

public class JavaBeanArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int FIRST_CONSTRUCTOR_INDEX = 0;

    @Override
    public boolean support(MethodParameter methodParameter) {
        return !methodParameter.isPrimitiveType();
    }

    @Override
    public Object resolve(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        Object value = null;
        try {
            Constructor<?>[] constructors = methodParameter.getType().getConstructors();
            value = constructors[FIRST_CONSTRUCTOR_INDEX].newInstance();
            Field[] fields = methodParameter.getType().getDeclaredFields();
            setFields(request, value, fields);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void setFields(HttpServletRequest request, Object obj, Field[] fields) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(obj, decideParameter(request.getParameter(field.getName()), field.getType()));
        }
    }

}
