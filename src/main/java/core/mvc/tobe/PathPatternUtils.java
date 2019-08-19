package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternUtils {

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    static {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
    }

    public static PathPattern parse(String path) {
        return PATH_PATTERN_PARSER.parse(path);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
