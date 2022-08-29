package core.mvc.tobe.resolver;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

public class PathPatternUtils {
    private static final PathPatternParser parser;

    private PathPatternUtils() {
    }

    static {
        parser = new PathPatternParser();
        parser.setMatchOptionalTrailingSeparator(true);
    }

    public static Map<String, String> getUriVariables(String path, String rawPath) {
        verifyPath(path);
        verifyPath(rawPath);
        return parser.parse(path).matchAndExtract(toPathContainer(rawPath)).getUriVariables();
    }

    private static void verifyPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("전달 받은 path 은 null 이거나 비어있을 수 없습니다.");
        }
    }

    private static PathContainer toPathContainer(String path) {
        return PathContainer.parsePath(path);
    }
}
