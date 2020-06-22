package core.mvc.support.resolvers;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.support.MethodParameter;
import core.mvc.utils.type.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * See link below:
 * {@link core.annotation.web.PathVariable}
 *
 * @author hyeyoom
 */
public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(PathVariableMethodArgumentResolver.class);

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    static {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Optional
                .ofNullable(methodParameter.getParameterAnnotation(PathVariable.class))
                .isPresent();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        final String uriToCheck = request.getRequestURI();
        final Map<String, String> map = extractValues(methodParameter, uriToCheck);
        if (map == null) {
            return null;
        }
        final String raw = map.get(methodParameter.getParameterName());
        final Object converted = TypeConverter.convert(raw, methodParameter.getParameterType());
        log.debug("converted data: {}", converted);
        return converted;
    }

    private static Map<String, String> extractValues(MethodParameter methodParameter, String actualUri) {
        final RequestMapping mappingInfo = methodParameter.getMethodAnnotation(RequestMapping.class);
        final String patternUri = mappingInfo.value();
        final PathPattern.PathMatchInfo pathMatchInfo = parse(patternUri).matchAndExtract(toPathContainer(actualUri));
        if (pathMatchInfo != null) {
            return pathMatchInfo.getUriVariables();
        }
        return null;
    }

    private static PathPattern parse(String patternUri) {
        // TODO: 파싱하는 비용이 적지 않을텐데 캐시를 도입해도 좋을 듯
        return PATH_PATTERN_PARSER.parse(patternUri);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
