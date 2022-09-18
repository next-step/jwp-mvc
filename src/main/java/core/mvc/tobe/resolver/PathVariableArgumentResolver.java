package core.mvc.tobe.resolver;


import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.tobe.MethodParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class PathVariableArgumentResolver implements ArgumentResolver {
    @Override
    public boolean support(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  String parameterName) {

        PathVariable pathVariable = methodParameter.getParameterAnnotation(PathVariable.class);
        String key = StringUtils.isNotBlank(pathVariable.name()) ? pathVariable.name() : parameterName;

        String path = methodParameter.getMethodAnnotation(RequestMapping.class).value();
        PathPattern pattern = PathPatternParser.defaultInstance.parse(path);
        PathContainer pathContainer = PathContainer.parsePath(request.getRequestURI());

        PathPattern.PathMatchInfo pathMatchInfo = pattern.matchAndExtract(pathContainer);
        Map<String, String> uriVariables = pathMatchInfo.getUriVariables();

        String result = uriVariables.getOrDefault(key, null);
        return cast(methodParameter.getParameterType(), result);
    }

}
