package core.mvc.tobe.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.utils.TypeUtils;

public class PathVariableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    public PathVariableHandlerMethodArgumentResolver() {
        this.pathPatternParser.setMatchOptionalTrailingSeparator(true);
    }

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolve(String parameterName, Parameter parameter, HttpServletRequest request, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new IllegalArgumentException("Pathvariable 은 @RequestMapping필요합니다");
        }

        var urlPattern = method.getDeclaredAnnotation(RequestMapping.class)
            .value();

        var pathPattern = pathPatternParser.parse(urlPattern);
        var pathContainer = PathContainer.parsePath(request.getRequestURI());

        var value = pathPattern.matchAndExtract(pathContainer)
            .getUriVariables()
            .get(parameterName);

        return TypeUtils.stringToType(parameter.getType(), value);
    }
}
