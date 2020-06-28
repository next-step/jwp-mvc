package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static core.mvc.tobe.ParameterUtils.isRequestType;

public class JavaBeanArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int FIRST_CONSTRUCTOR_INDEX = 0;

    @Override
    public boolean support(Method method) {
        return !isRequestType(method);
    }

    @Override
    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] values = new Object[parameterTypes.length];
        try {
            for (int i = 0; i < parameterTypes.length; i++) {
                Constructor<?>[] constructors = parameterTypes[i].getConstructors();
                Object obj = constructors[FIRST_CONSTRUCTOR_INDEX].newInstance();
                Field[] fields = parameterTypes[i].getDeclaredFields();
                setFields(request, obj, fields);
                values[i] = obj;
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return values;
    }

    private void setFields(HttpServletRequest request, Object obj, Field[] fields) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(obj, ParameterUtils.decideParameter(request.getParameter(field.getName()), field.getType()));
        }
    }

}
