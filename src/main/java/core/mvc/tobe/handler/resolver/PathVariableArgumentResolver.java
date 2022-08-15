package core.mvc.tobe.handler.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
import core.mvc.tobe.handler.resolver.utils.TypeUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Map;

public class PathVariableArgumentResolver extends AbstractNamedSimpleTypeArgumentResolver implements ArgumentResolver {

    public PathVariableArgumentResolver(SimpleTypeConverter simpleTypeConverter) {
        super(simpleTypeConverter);
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
        String name = pathVariable.name();
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

        PathPattern parse = parse(urlPattern);
        boolean matches = parse.matches(toPathContainer(actualUrl));
        if (!matches) {
            throw new RuntimeException("패턴이 일치하지 않는단다!");
        }

        Map<String, String> variables = parse
                .matchAndExtract(toPathContainer(actualUrl))
                .getUriVariables();

        String value = variables.get(parameterName);
        return value;
    }

    private String getRequestUrlPattern(Parameter parameter) {
        Executable executable = parameter.getDeclaringExecutable();
        RequestMapping requestMapping = executable.getAnnotation(RequestMapping.class);
        String urlPattern = requestMapping.value();
        return urlPattern;
    }

    private PathPattern parse(String path) {
        PathPatternParser patternParser = new PathPatternParser();
        patternParser.setMatchOptionalTrailingSeparator(true);
        return patternParser.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
