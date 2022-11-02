package core.mvc.resolver;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternUtil {
    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    public static String getUriVariable(final String pattern, final String requestURI, final String key) {
        PathPattern pathPattern = PATH_PATTERN_PARSER.parse(pattern);
        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(PathContainer.parsePath(requestURI));
        return pathMatchInfo.getUriVariables().get(key);
    }
}
