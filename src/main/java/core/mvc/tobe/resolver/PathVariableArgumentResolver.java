package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class PathVariableArgumentResolver extends AbstractArgumentResolver {

    private static final PathPatternParser PATTERN_PARSER =new PathPatternParser();
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {

        String requestURI = request.getRequestURI();
        String annotationURI = getAnnotationURI(methodParameter.getMethod());
        String argumentName = getArgumentName(methodParameter.getMethod(), methodParameter.getParameterIndex());

        return TypeConverter.cast(methodParameter.getParameterType(), getArgumentValue(annotationURI, requestURI, argumentName));
    }

    public String getArgumentValue(String annotationURI, String requestURI, String argumentName) {
        PathPattern.PathMatchInfo pathMatchInfo = PATTERN_PARSER.parse(annotationURI).matchAndExtract(PathContainer.parsePath(requestURI));
        Map<String, String> uriVariables = Optional.ofNullable(pathMatchInfo.getUriVariables()).orElse(Collections.emptyMap());

        return uriVariables.get(argumentName);
    }

    public String getAnnotationURI(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return annotation.value();
    }

}
