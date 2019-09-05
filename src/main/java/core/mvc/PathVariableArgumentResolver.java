package core.mvc;

import core.annotation.web.PathVariable;
import core.mvc.support.PathVariableUtils;
import core.mvc.tobe.MethodParameter;
import org.springframework.http.server.PathContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static core.mvc.support.PathVariableUtils.parse;
import static core.mvc.support.PathVariableUtils.toPathContainer;

public class PathVariableArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {

        String url = Arrays.stream(parameter.getUrls())
                .filter(PathVariableUtils::hasPathVariable)
                .findFirst()
                .orElse("");
        PathContainer pathContainer = toPathContainer(request.getRequestURI());
        Map<String, String> variables = Objects.requireNonNull(parse(url).matchAndExtract(pathContainer)).getUriVariables();
        String arg = variables.get(parameter.getParameterName());

        return PathVariableUtils.toPrimitiveValue(parameter.getParameterType(), arg);
    }
}