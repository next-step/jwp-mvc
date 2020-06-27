package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class PathVariableResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object[] resolve(Method method, HttpServletRequest request, HttpServletResponse response) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        Map<String, String> variables = parse(annotation.value())
                .matchAndExtract(toPathContainer(request.getRequestURI())).getUriVariables();

        return variables.values().toArray();
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
