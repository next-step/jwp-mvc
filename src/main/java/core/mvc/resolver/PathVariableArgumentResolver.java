package core.mvc.resolver;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import core.annotation.web.RequestMapping;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathVariableArgumentResolver implements ArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Annotation[] annotations = methodParameter.getAnnotations();
        return Arrays.stream(annotations).anyMatch(annotation -> annotation.annotationType() == PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {

        String pathVariableValue = getPathVarialbeValue(methodParameter);

        Method method = methodParameter.getMethod();
        String controllerUrl = getControllerUrl(method);
        String methodUrl = getMethodUrl(method);

        Map<String, String> pathVariables = parse(controllerUrl + methodUrl)
                .matchAndExtract(PathContainer.parsePath(request.getRequestURI()))
                .getUriVariables();
        return pathVariables.get(pathVariableValue);
    }

    public String getPathVarialbeValue(MethodParameter methodParameter) {
        PathVariable pathVariable = (PathVariable) Arrays.stream(methodParameter.getAnnotations())
                .filter(annotation -> annotation.annotationType() == PathVariable.class)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return pathVariable.value();
    }

    public String getControllerUrl(Method method) {
        Controller controllerAnnotation = method.getDeclaringClass().getAnnotation(Controller.class);
        return controllerAnnotation.value();
    }

    public String getMethodUrl(Method method) {
        RequestMapping requestMappingAnnotation = (RequestMapping) Arrays.stream(method.getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType() == RequestMapping.class)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return requestMappingAnnotation.value();
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }
}
