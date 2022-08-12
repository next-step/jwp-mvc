package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static org.springframework.web.util.pattern.PathPattern.PathMatchInfo;

public class PathVariableMethodArgumentResolver implements ArgumentResolver {

    private final PathPatternParser pathPatternParser = new PathPatternParser();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        String requestPath = getRequestPath(parameter);
        Map<String, String> variables = getUriVariables(request.getRequestURI(), requestPath);

        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        String key = getVariableKey(pathVariable, parameter.getParameterName());
        String value = variables.get(key);

        TypeConverter converter = TypeConverter.valueOf(parameter.getType());
        return converter.convert(value);
    }

    private String getRequestPath(MethodParameter parameter) {
        Method method = parameter.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return requestMapping.value();
    }

    private Map<String, String> getUriVariables(String requestURI, String requestPath) {
        PathContainer pathContainer = PathContainer.parsePath(requestURI);
        PathPattern pattern = pathPatternParser.parse(requestPath);
        PathMatchInfo pathMatchInfo = pattern.matchAndExtract(pathContainer);
        if (pathMatchInfo == null) {
            return Collections.emptyMap();
        }
        return pathMatchInfo.getUriVariables();
    }

    private String getVariableKey(PathVariable pathVariable, String parameterName) {
        String name = pathVariable.name();
        if (!StringUtils.isBlank(name)) {
            return name;
        }
        return parameterName;
    }
}
