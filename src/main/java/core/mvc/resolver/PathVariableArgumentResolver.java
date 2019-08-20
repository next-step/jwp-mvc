package core.mvc.resolver;

import core.mvc.MethodParameter;
import org.springframework.web.util.pattern.PathPattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static core.mvc.utils.PathPatternMatcher.toPathContainer;

public class PathVariableArgumentResolver extends AbstractHandlerMethodArgumentResolver {
    private PathPattern pattern;

    public PathVariableArgumentResolver(PathPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isPathVariable();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> pathVariables = pattern
                .matchAndExtract(toPathContainer(request.getRequestURI())).getUriVariables();

        return getArgument(parameter, pathVariables.get(parameter.getName()));
    }
}
