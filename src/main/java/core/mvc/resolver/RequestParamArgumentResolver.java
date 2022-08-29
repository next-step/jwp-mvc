package core.mvc.resolver;

import core.annotation.web.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public class RequestParamArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supports(RequestParameter requestParameter) {
        Annotation[] parameterAnnotations = requestParameter.getAnnotations();
        return Arrays.stream(parameterAnnotations)
                .anyMatch(annotation -> annotation.annotationType() == RequestParam.class);
    }

    @Override
    public Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        RequestParam requestParamAnnotation = (RequestParam) Arrays.stream(requestParameter.getAnnotations())
                .filter(annotation -> annotation.annotationType() == RequestParam.class)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        String value = requestParamAnnotation.value();
        Class<?> classType = requestParameter.getClassType();

        return returnValue(request.getParameter(value), classType);
    }

    private Object returnValue(String value, Class<?> classType) {
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
