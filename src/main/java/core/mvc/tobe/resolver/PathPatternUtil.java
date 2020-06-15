package core.mvc.tobe.resolver;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternUtil {
    private static final PathPatternParser pathPatternParser = initPathPatternParser();

    private static PathPatternParser initPathPatternParser() {
        PathPatternParser ppp = new PathPatternParser();
        ppp.setMatchOptionalTrailingSeparator(true);
        return ppp;
    }

    public static PathPattern parse(String path) {
        return pathPatternParser.parse(path);
    }

    public static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }

        return PathContainer.parsePath(path);
    }
}
