package core.mvc.tobe.method.support;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.method.HandlerMethodArgumentResolver;
import core.mvc.tobe.method.MethodParameter;
import core.mvc.tobe.method.SimpleTypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.util.Assert;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final PathPatternParser parser = new PathPatternParser();
    private final SimpleTypeConverter typeConverter;

    public PathVariableMethodArgumentResolver(SimpleTypeConverter typeConverter) {
        Assert.notNull(typeConverter, "typeConverter가 null이어선 안됩니다.");
        this.typeConverter = typeConverter;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(RequestMapping.class) && parameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        PathPattern.PathMatchInfo pathMatchInfo = parser.parse(requestMapping(parameter).value())
                .matchAndExtract(PathContainer.parsePath(request.getRequestURI()));
        validatePathMatch(parameter, pathMatchInfo);
        return typeConverter.convert(pathMatchInfo.getUriVariables().get(argumentName(parameter)), parameter.getType());
    }

    private String argumentName(MethodParameter parameter) {
        PathVariable pathVariable = parameter.parameterAnnotation(PathVariable.class);
        if (StringUtils.isBlank(pathVariable.value())) {
            return parameter.getName();
        }
        return pathVariable.value();
    }

    private void validatePathMatch(MethodParameter parameter, PathPattern.PathMatchInfo pathMatchInfo) {
        if (pathMatchInfo == null) {
            throw new IllegalStateException(
                    String.format("%s는 지원하지 않는 메서드 타입 입니다.", parameter)
            );
        }
    }

    private RequestMapping requestMapping(MethodParameter parameter) {
        return parameter.methodAnnotation(RequestMapping.class);
    }
}
