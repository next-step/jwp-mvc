package core.mvc.supporter;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    @Override
    public Object resolveArgument(Parameter parameter, Method method, HttpServletRequest httpServletRequest, int index) throws Exception {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String requestURI = httpServletRequest.getRequestURI();

        PathPattern.PathMatchInfo pathMatchInfo = parse(requestMapping.value())
                .matchAndExtract(toPathContainer(requestURI));

        if (Objects.isNull(pathMatchInfo)) {
            return null;
        }

        Map<String, String> variables = pathMatchInfo.getUriVariables();
        String value = variables.get(nameDiscoverer.getParameterNames(method)[index]);

        return TypeConverter.convert(parameter.getType(), value);
    }


    private static PathPattern parse(String path) {
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
}
