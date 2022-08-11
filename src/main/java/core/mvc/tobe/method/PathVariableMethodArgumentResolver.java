package core.mvc.tobe.method;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.util.Assert;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final PathPatternParser PATTERN_PARSER = new PathPatternParser();

    private final SimpleTypeConverter converter;

    private PathVariableMethodArgumentResolver(SimpleTypeConverter converter) {
        Assert.notNull(converter, "'converter' must not be null");
        this.converter = converter;
    }

    static PathVariableMethodArgumentResolver from(SimpleTypeConverter converter) {
        return new PathVariableMethodArgumentResolver(converter);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(RequestMapping.class)
                && parameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        PathPattern.PathMatchInfo pathMatchInfo = PATTERN_PARSER.parse(requestMapping(parameter).value())
                .matchAndExtract(PathContainer.parsePath(request.getRequestURI()));
        validatePathMach(parameter, pathMatchInfo);
        return converter.convert(pathMatchInfo.getUriVariables().get(argumentName(parameter)), parameter.type());
    }

    private String argumentName(MethodParameter parameter) {
        PathVariable pathVariable = parameter.parameterAnnotation(PathVariable.class);
        if (StringUtils.isBlank(pathVariable.value())) {
            return parameter.name();
        }
        return pathVariable.value();
    }

    private void validatePathMach(MethodParameter parameter, PathPattern.PathMatchInfo pathMatchInfo) {
        if (pathMatchInfo == null) {
            throw new IllegalStateException(
                    String.format("unsupported method(%s) type. supportsParameter should be called first", parameter));
        }
    }

    private RequestMapping requestMapping(MethodParameter parameter) {
        return parameter.methodAnnotation(RequestMapping.class);
    }
}
