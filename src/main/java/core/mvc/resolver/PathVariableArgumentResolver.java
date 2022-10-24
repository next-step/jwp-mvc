package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.MethodParameter;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class PathVariableArgumentResolver implements MethodArgumentResolver {
    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasAnnotationInParameter(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        RequestMapping rm = parameter.getMethod().getAnnotation(RequestMapping.class);
        PathPattern.PathMatchInfo pathMatchInfo = getPathMatchInfo(rm, request.getRequestURI());

//        if (Objects.isNull(pathMatchInfo)) {
//            return getValueWithMatchingType(methodParameter.getParameterType(), null);
//        }

        Map<String, String> uriVariables = pathMatchInfo.getUriVariables();
        String pathValue = parameter.getParameter().getAnnotation(PathVariable.class).value();

        String object = "";
        if(pathValue.isEmpty()) {
            object = uriVariables.get(parameter.getParameterName());
        }
        else {
            object = uriVariables.get(pathValue);
        }

        return ResolverUtility.convertPrimitiveType(parameter.getParameterType(), object);
    }

    private static PathPattern.PathMatchInfo getPathMatchInfo(RequestMapping requestMapping, final String requestURI) {
        PathPattern pathPattern = PATH_PATTERN_PARSER.parse(requestMapping.value());
        return pathPattern.matchAndExtract(PathContainer.parsePath(requestURI));
    }

}
