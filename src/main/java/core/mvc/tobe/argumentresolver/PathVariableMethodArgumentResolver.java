package core.mvc.tobe.argumentresolver;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.MethodParameter;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
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

    private String getRequestPath(MethodParameter methodParameter) {
        Method method = methodParameter.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return requestMapping.value();
    }

}
