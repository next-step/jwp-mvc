package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BeanTypeArgumentResolver implements ArgumentResolver {

    @Override
    public boolean equalsTo(final Class parameterType, final Method method) {
        return !PathVariableArgumentResolver.isPathVariable(method)
                && (!parameterType.equals(String.class)
                && !parameterType.isPrimitive()
                && !parameterType.isInterface());
    }

    @Override
    public Object getParameterValue(final HttpServletRequest request, final HttpServletResponse response, final ResolverParameter resolverParameter) throws Exception {
        Class parameterType = resolverParameter.getType();
        final Field[] declaredFields = parameterType.getDeclaredFields();
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
        Constructor<?> declaredConstructor = Arrays.stream(parameterType.getConstructors())
                .max((o1, o2) -> Math.max(o1.getParameterCount(), o2.getParameterCount()))
                .get();

        return declaredConstructor.newInstance(values);
    }

}
