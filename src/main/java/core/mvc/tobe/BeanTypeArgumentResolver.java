package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BeanTypeArgumentResolver extends ArgumentResolver {

    private Class clazz;
    private Method method;

    public BeanTypeArgumentResolver(final Class clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
        applyExecution(method, this);
    }

    @Override
    public void applyExecution(final Method method, final HandlerExecution handlerExecution) {
        super.applyExecution(method, handlerExecution);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        for (final Class<?> object : method.getParameterTypes()) {
            final Field[] declaredFields = object.getDeclaredFields();
            Object[] values = new Object[declaredFields.length];
            for (int i = 0; i < declaredFields.length; i++) {
                String value = request.getParameter(declaredFields[i].getName());
                values[i] = value;
                if (declaredFields[i].getType().equals(int.class)) {
                    values[i] = Integer.parseInt(value);
                }

                if (declaredFields[i].getType().equals(long.class)) {
                    values[i] = Long.parseLong(value);
                }
            }

            Constructor<?> declaredConstructor = Arrays.stream(object.getConstructors())
                    .max((o1, o2) -> Math.max(o1.getParameterCount(), o2.getParameterCount()))
                    .get();

            declaredConstructor.setAccessible(true);
            return (ModelAndView) method.invoke(clazz.newInstance(), declaredConstructor.newInstance(values));
        }

        return super.handle(request, response);
    }
}
