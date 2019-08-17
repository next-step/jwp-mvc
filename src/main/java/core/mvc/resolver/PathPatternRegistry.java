package core.mvc.resolver;

import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.HashMap;
import java.util.Map;

public class PathPatternRegistry {

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();
    private final Map<String, PathPattern> pathPatterns = new HashMap<>();

    public PathPattern getPathPattern(String uri) {
        if (pathPatterns.containsKey(uri)) {
            return pathPatterns.get(uri);
        }

        PathPattern pathPattern = parse(uri);
        pathPatterns.put(uri, pathPattern);
        return pathPattern;
    }

    private static PathPattern parse(String path) {
        return PATH_PATTERN_PARSER.parse(path);
    }
}
