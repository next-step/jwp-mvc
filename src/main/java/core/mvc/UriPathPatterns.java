package core.mvc;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.HashMap;
import java.util.Map;

public class UriPathPatterns {

    private final PathPatternParser pathPatternParser = new PathPatternParser();
    private Map<String, PathPattern> cache = new HashMap<>();

    private UriPathPatterns() {
        this.pathPatternParser.setMatchOptionalTrailingSeparator(true);
    }

    public static UriPathPatterns getInstance() {
        return Lazy.INSTANCE;
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    public Map<String, String> getPathVariables(String pathPattern, String requestUri) {
        return getPattern(pathPattern).matchAndExtract(toPathContainer(requestUri))
                .getUriVariables();
    }

    public PathPattern getPattern(String pathPattern) {
        return cache.computeIfAbsent(pathPattern, this::parsePattern);
    }

    private PathPattern parsePattern(String pathPattern) {
        return this.pathPatternParser.parse(pathPattern);
    }

    private static class Lazy {
        private static UriPathPatterns INSTANCE = new UriPathPatterns();
    }

}
