package core.utils;

import java.util.Map;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * Created by iltaek on 2020/06/30 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class PathVariableParser {

    private PathVariableParser() {
    }

    public static Map<String, String> parsePathVariable(String pathPattern, String requestURI) {
        return parsePathVariablePattern(pathPattern)
            .matchAndExtract(toPathContainer(requestURI))
            .getUriVariables();
    }

    private static PathPattern parsePathVariablePattern(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

}
