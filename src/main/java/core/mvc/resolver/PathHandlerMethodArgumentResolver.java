package core.mvc.resolver;

import core.annotation.web.PathVariable;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern.PathMatchInfo;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final PathPatternParser pp;
    private final ParameterNameDiscoverer nameDiscoverer;


    public PathHandlerMethodArgumentResolver() {
        pp = new PathPatternParser();
        nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Parameter parameter = methodParameter.toParameter();
        return parameter.isAnnotationPresent(PathVariable.class) && ParameterUtils
            .supportPrimitiveType(methodParameter);
    }

    @Override
    public Object resolve(MethodParameter methodParameter, String mappingUrl, HttpServletRequest httpRequest) {
        if (!supportsParameter(methodParameter)) {
            throw new RuntimeException("unSupports Parameter");
        }

        String variable = parse(methodParameter, mappingUrl, httpRequest);
        return ParameterUtils.convertToPrimitiveType(methodParameter.toParameter(), variable);
    }

    private String parse(MethodParameter methodParameter, String mappingUrl,
        HttpServletRequest httpRequest) {
        PathMatchInfo pathMatchInfo = pp.parse(mappingUrl)
            .matchAndExtract(PathContainer.parsePath(httpRequest.getRequestURI()));
        String name = getPathName(methodParameter);
        return pathMatchInfo.getUriVariables().get(name);
    }

    private String getPathName(MethodParameter methodParameter) {
        Parameter parameter = methodParameter.toParameter();
        String name = parameter.getAnnotation(PathVariable.class).value();
        if (name.isEmpty()) {
            name = methodParameter.getParameterName();
        }
        return name;
    }

}
