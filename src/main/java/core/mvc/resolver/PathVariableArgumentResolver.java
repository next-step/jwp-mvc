package core.mvc.resolver;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class PathVariableArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supports(RequestParameter requestParameter) {
        Annotation[] parameterAnnotations = requestParameter.getAnnotations();
        return Arrays.stream(parameterAnnotations)
                .anyMatch(annotation -> annotation.annotationType() == PathVariable.class);
    }

    @Override
    public Object resolveArgument(RequestParameter requestParameter, HttpServletRequest request, HttpServletResponse response) {
        String pathVariableValue = getPathVariableValue(requestParameter);

        Method method = requestParameter.getMethod();
        String controllerRequestUrl = getControllerRequestUrl(method);
        String requestUrl = getMethodRequestUrl(method);

        Map<String, String> uriVariables = parse(controllerRequestUrl + requestUrl)
                .matchAndExtract(PathContainer.parsePath(request.getRequestURI()))
                .getUriVariables();

        return returnValue(uriVariables.get(pathVariableValue), requestParameter.getClassType());
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

    private String getControllerRequestUrl(Method method) {
        Controller annotation = method.getDeclaringClass().getAnnotation(Controller.class);
        return annotation.value();
    }

    private String getPathVariableValue(RequestParameter requestParameter) {
        PathVariable pathVariableAnnotation = (PathVariable) Arrays.stream(requestParameter.getAnnotations())
                .filter(annotation -> annotation.annotationType() == PathVariable.class)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        String pathVariableValue = pathVariableAnnotation.value();
        return pathVariableValue;
    }

    private String getMethodRequestUrl(Method method) {
        RequestMapping requestMappingAnnotation = (RequestMapping) Arrays.stream(method.getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType() == RequestMapping.class)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        String requestUrl = requestMappingAnnotation.value();
        return requestUrl;
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

}
