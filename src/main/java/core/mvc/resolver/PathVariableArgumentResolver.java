package core.mvc.resolver;

import core.annotation.Component;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class PathVariableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final PathPatternParser pathPatternParser = PathPatternParser.defaultInstance;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        RequestMapping requestMapping = parameter.getMethod().getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        String requestURI = request.getRequestURI();

        Map<String, String> variables = getUriVariables(uri, requestURI);

        String value = variables.get(parameter.getParameterName());
        return convert(parameter, value);
    }

    private Map<String, String> getUriVariables(String uri, String requestURI) {
        return pathPatternParser.parse(uri)
                .matchAndExtract(PathContainer.parsePath(requestURI))
                .getUriVariables();
    }

    private Object convert(MethodParameter parameter, String value) {
        Object convert = PrimitiveType.convert(parameter.getParameterType(), value);
        return convert;
    }
}
