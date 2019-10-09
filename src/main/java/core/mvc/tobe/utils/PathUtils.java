package core.mvc.tobe.utils;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public final class PathUtils {

    private PathUtils() { }

    private static final PathPatternParser pathPatternParser;

    static {
        pathPatternParser = new PathPatternParser();
        pathPatternParser.setMatchOptionalTrailingSeparator(true);
    }

    public static boolean matches(final String pattern,
                                  final String path) {
        return toPathContainer(path)
                .map(pathContainer -> toPathPattern(pattern).matches(pathContainer))
                .orElse(false);
    }

    public static Map<String, String> parse(final String pattern,
                                            final String path) {
        return toPathContainer(path)
                .map(pathContainer -> toPathPattern(pattern).matchAndExtract(pathContainer))
                .map(PathPattern.PathMatchInfo::getUriVariables)
                .orElseGet(Collections::emptyMap);
    }

    public static Optional<String> parse(final String pattern,
                                         final String path,
                                         final String target) {
        return Optional.ofNullable(parse(pattern, path).get(target));
    }

    private static PathPattern toPathPattern(final String pattern) {
        return pathPatternParser.parse(pattern);
    }

    private static Optional<PathContainer> toPathContainer(final String path) {
        return Optional.ofNullable(path)
                .map(PathContainer::parsePath);
    }
}
