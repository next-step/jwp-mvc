package core.mvc.tobe.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.utils.DataParser;
import core.mvc.tobe.MethodParameter;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return methodParameter.hasAnnotation(PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        PathPattern pathPattern = getPathPattern(methodParameter.getMethod());
        Map<String, String> uriVariables = pathPattern.matchAndExtract(PathContainer.parsePath((req.getRequestURI()))).getUriVariables();

        DataParser dataParser = DataParser.from(methodParameter.getType());
        return dataParser.parse(uriVariables.get(methodParameter.getName()));
    }

    private PathPattern getPathPattern(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        PathPatternParser ppp = new PathPatternParser();
        ppp.setMatchOptionalTrailingSeparator(true);
        return ppp.parse(annotation.value());
    }
}
