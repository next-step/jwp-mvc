package utils;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternParserUtil {

    public static PathPattern parse(String path) {
        final PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    public static boolean match(String patternUrl, String requestUrl) {
        if (requestUrl == null) {
            return false;
        }

        final PathPattern pp = parse(patternUrl);
        return pp.matches(PathContainer.parsePath(requestUrl));
    }

}
