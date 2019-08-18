package core.mvc.resolver;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class PathVariableResolver {

    private static final PathPatternRegistry PATH_PATTERN_REGISTRY = new PathPatternRegistry();

    public static Map<String, String> getPathVariables(String requestMappingValue, String requestUri) {
        PathPattern pathPattern = PATH_PATTERN_REGISTRY.getPathPattern(requestMappingValue);

        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(toPathContainer(requestUri));
        if (Objects.isNull(pathMatchInfo)) {
            return Collections.emptyMap();
        }
        return pathMatchInfo.getUriVariables();
    }

    private static PathContainer toPathContainer(String uri) {
        return PathContainer.parsePath(uri);
    }

}
