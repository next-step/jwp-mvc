package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.ClassUtils;

public class WrapperMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        Class<?> parameterType = methodParameter.getParameterType();
        String value = request.getParameter(methodParameter.getParameterName());

        if (parameterType.equals(Integer.class)) {
            return Integer.valueOf(value);
        }

        if (parameterType.equals(Long.class)) {
            return Long.valueOf(value);
        }

        if (parameterType.equals(Double.class)) {
            return Double.valueOf(value);
        }

        if (parameterType.equals(Float.class)) {
            return Float.valueOf(value);
        }

        return value;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return ClassUtils.isPrimitiveOrWrapper(methodParameter.getParameterType());
    }
}
