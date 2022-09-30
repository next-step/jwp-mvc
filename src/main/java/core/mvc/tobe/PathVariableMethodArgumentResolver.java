package core.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

import core.annotation.web.RequestMapping;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        String servletPath = request.getServletPath();

        PathPattern pathPattern = parse(getRequestPath(methodParameter));
        PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(toPathContainer(servletPath));
        Map<String, String> uriVariables = pathMatchInfo.getUriVariables();

        return Long.parseLong(uriVariables.get(methodParameter.getParameterName()));
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasPathVariableAnnotation();
    }

    private PathPattern parse(String path) {
        PathPatternParser pathPatternParser = new PathPatternParser();
        pathPatternParser.setMatchOptionalTrailingSeparator(true);
        return pathPatternParser.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    private String getRequestPath(MethodParameter methodParameter) {
        Method method = methodParameter.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return requestMapping.value();
    }
}
