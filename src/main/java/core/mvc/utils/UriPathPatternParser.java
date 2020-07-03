package core.mvc.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UriPathPatternParser {

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    static {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
    }

    public static PathPattern parse(String uri) {
        return PATH_PATTERN_PARSER.parse(uri);
    }

    public static boolean match(String uri, String actualPath) {
        PathPattern pathPattern = parse(uri);

        return pathPattern.matches(PathContainer.parsePath(actualPath));
    }

    public static boolean mismatch(String uri, String actualPath) {
        return !match(uri, actualPath);
    }

    public static Map<String, String> getUriVariables(String uri, String actualPath) {
        PathPattern pathPattern = parse(uri);

        return pathPattern.matchAndExtract(PathContainer.parsePath(actualPath))
                .getUriVariables();
    }
}
