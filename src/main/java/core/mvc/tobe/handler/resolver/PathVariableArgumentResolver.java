package core.mvc.tobe.handler.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
import core.mvc.tobe.handler.resolver.utils.TypeUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Map;

public class PathVariableArgumentResolver implements ArgumentResolver {

    @Override
    public boolean support(NamedParameter parameter) {
        return TypeUtils.isSimpleType(parameter.getType()) &&
                parameter.getParameter().isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolve(NamedParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        PathVariable pathVariable = parameter.getParameter().getAnnotation(PathVariable.class);
        String name = getParameterName(parameter, pathVariable);

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

        String valueAsString = variables.get(name);

        Object convertedValue = new SimpleTypeConverter().convert(valueAsString, parameter.getType());

        return convertedValue;
    }

    private String getRequestUrlPattern(Parameter parameter) {
        Executable executable = parameter.getDeclaringExecutable();
        RequestMapping requestMapping = executable.getAnnotation(RequestMapping.class);
        String urlPattern = requestMapping.value();
        return urlPattern;
    }

    private String getParameterName(NamedParameter parameter, PathVariable pathVariable) {
        String name = pathVariable.name();
        if (!name.isEmpty()) {
            return name;
        }
        String argumentName = parameter.getName();
        return argumentName;
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
