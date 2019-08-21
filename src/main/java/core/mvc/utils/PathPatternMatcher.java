package core.mvc.utils;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternMatcher {
    private static final PathPatternParser parser;

    private PathPatternMatcher() {
    }

    static {
        parser = new PathPatternParser();
        parser.setMatchOptionalTrailingSeparator(true);
    }

    public static PathPattern parse(String url) {
        return parser.parse(url);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }

        return PathContainer.parsePath(path);
    }

    public static boolean isUrlMatch(String url, String targetUrl) {
        return parse(url).matches(toPathContainer(targetUrl));
    }
}
