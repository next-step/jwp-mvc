package core.mvc.tobe;

import core.annotation.web.PathVariable;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final PathPattern pathPattern;

    public PathVariableMethodArgumentResolver(PathPattern pathPattern) {
        this.pathPattern = pathPattern;
    }

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req) throws Exception {
        Map<String, String> uriVariables = pathPattern.matchAndExtract(PathContainer.parsePath((req.getRequestURI()))).getUriVariables();

        DataParser dataParser = DataParser.from(methodParameter.getType());
        return dataParser.parse(uriVariables.get(methodParameter.getName()));
    }
}
