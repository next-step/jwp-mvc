package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternUtil {

    public static PathPatternParser PARSER = new PathPatternParser();

    public static PathPattern getPathPattern(String path) {
        PARSER.setMatchOptionalTrailingSeparator(true);
        return PARSER.parse(path);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }

        return PathContainer.parsePath(path);
    }

}
