package core.mvc.tobe.handler.resolver.utils;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PatternMatcher {
    private static final Map<String, PathPattern> PATTERN_CACHE = new ConcurrentHashMap<>();

    public boolean matches(String urlPattern, String targetUrl) {
        return parse(urlPattern)
                .matches(toPathContainer(targetUrl));
    }

    public Map<String, String> getUrlVariables(String urlPattern, String targetUrl) {
        return parse(urlPattern)
                .matchAndExtract(toPathContainer(targetUrl))
                .getUriVariables();
    }

    private PathPattern parse(String path) {
        return PATTERN_CACHE.computeIfAbsent(path, this::createPathPattern);
    }

    private PathPattern createPathPattern(String path) {
        PathPatternParser pathPatternParser = new PathPatternParser();
        pathPatternParser.setMatchOptionalTrailingSeparator(true);
        return pathPatternParser.parse(path);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }

        return PathContainer.parsePath(path);
    }
}
