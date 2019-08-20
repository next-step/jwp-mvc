package core.mvc.resolver;

import core.annotation.web.RequestMapping;
import core.mvc.MethodParameter;
import core.mvc.utils.PathPatternMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

import static core.mvc.utils.PathPatternMatcher.toPathContainer;

public class PathVariableArgumentResolver extends AbstractHandlerMethodArgumentResolver {
    private Method method;

    public PathVariableArgumentResolver(Method method) {
        this.method = method;
    }

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isPathVariable();
    }

    @Override
    public Object getMethodArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        Map<String, String> pathVariables = PathPatternMatcher.parse(annotation.value())
                .matchAndExtract(toPathContainer(request.getRequestURI())).getUriVariables();

        return getArgument(parameter, pathVariables.get(parameter.getName()));
    }
}
