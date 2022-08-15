package core.mvc.tobe.handler.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.handler.resolver.utils.PatternMatcher;
import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
import core.mvc.tobe.handler.resolver.utils.TypeUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Map;

public class PathVariableArgumentResolver extends AbstractNamedSimpleTypeArgumentResolver implements ArgumentResolver {

    private final PatternMatcher patternMatcher;

    public PathVariableArgumentResolver(SimpleTypeConverter simpleTypeConverter, PatternMatcher patternMatcher) {
        super(simpleTypeConverter);
        this.patternMatcher = patternMatcher;
    }

    @Override
    public boolean support(NamedParameter parameter) {
        return TypeUtils.isSimpleType(parameter.getType()) &&
                parameter.getParameter().isAnnotationPresent(PathVariable.class);
    }

    @Override
    protected String getParameterName(NamedParameter parameter) {
        PathVariable pathVariable = parameter.getParameter().getAnnotation(PathVariable.class);
        String parameterName = getParameterName(parameter, pathVariable);
        return parameterName;
    }

    private String getParameterName(NamedParameter parameter, PathVariable pathVariable) {
        String name = pathVariable.value();
        if (!name.isEmpty()) {
            return name;
        }
        String argumentName = parameter.getName();
        return argumentName;
    }

    @Override
    protected String getNamedValue(NamedParameter parameter, HttpServletRequest request, String parameterName) {
        String urlPattern = getRequestUrlPattern(parameter.getParameter());
        String actualUrl = request.getRequestURI();

        boolean matches = patternMatcher.matches(urlPattern, actualUrl);
        if (!matches) {
            throw new RuntimeException("패턴이 일치하지 않는단다!");
        }

        Map<String, String> variables = patternMatcher.getUrlVariables(urlPattern, actualUrl);
        String value = variables.get(parameterName);
        return value;
    }

    private String getRequestUrlPattern(Parameter parameter) {
        Executable executable = parameter.getDeclaringExecutable();
        RequestMapping requestMapping = executable.getAnnotation(RequestMapping.class);
        String urlPattern = requestMapping.value();
        return urlPattern;
    }
}
