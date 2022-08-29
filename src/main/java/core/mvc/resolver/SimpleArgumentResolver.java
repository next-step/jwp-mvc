package core.mvc.resolver;

import org.apache.commons.lang3.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(RequestParameter requestParameter) {
        return ClassUtils.isPrimitiveOrWrapper(requestParameter.getClassType())
                || CharSequence.class.isAssignableFrom(requestParameter.getClassType());
    }

    @Override
    public Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        Class<?> classType = requestParameter.getClassType();
        String value = request.getParameter(requestParameter.getParameterName());

        if (classType.equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (classType.equals(long.class)) {
            return Long.parseLong(value);
        }

        if (classType.equals(double.class)) {
            return Double.parseDouble(value);
        }

        if (classType.equals(float.class)) {
            return Float.parseFloat(value);
        }

        return value;
    }

}
