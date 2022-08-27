package next.support.resolver;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

public class PathAnalyzer {
    private static final PathPatternParser pathPatternParser = new PathPatternParser();

    private PathAnalyzer() {

    }

    public static boolean isSamePattern(String handlerUri, String requestedUri) {
        PathPattern pathPattern = pathPatternParser.parse(handlerUri);
        PathContainer pathContainer = PathContainer.parsePath(requestedUri);

        return pathPattern.matches(pathContainer);
    }

    public static String getVariables(String handlerUri, String requestedUri, String parameterName) {
        Map<String, String> variables = pathPatternParser.parse(handlerUri)
                .matchAndExtract(PathContainer.parsePath(requestedUri))
                .getUriVariables();

        return variables.get(parameterName);
    }
}
