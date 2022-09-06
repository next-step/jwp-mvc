package core.mvc.resolver;

import core.annotation.web.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public class RequestParamArgumentResolver implements ArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Annotation[] parameterAnnotations = methodParameter.getAnnotations();
        return Arrays.stream(parameterAnnotations).anyMatch(annotation -> annotation.annotationType() == RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestParam requestParamAnnotation = (RequestParam) Arrays.stream(methodParameter.getAnnotations())
                .filter(annotation -> annotation.annotationType() == RequestParam.class)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        String value = requestParamAnnotation.value();
        Class<?> parameterType = methodParameter.getParameterType();

        if (parameterType.equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (parameterType.equals(long.class)) {
            return Long.parseLong(value);
        }

        if (parameterType.equals(double.class)) {
            return Double.parseDouble(value);
        }

        if (parameterType.equals(float.class)) {
            return Float.parseFloat(value);
        }
        return parameterType;
    }
}
