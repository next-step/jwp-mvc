package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PathPatternUtils {

    public static boolean isMatch(String pattern, String url) {
        PathPattern pp = parse(pattern);
        return pp.matches(toPathContainer(url));
    }

    private static PathPattern parse(String pattern) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(pattern);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }

    public static Map<String, String> getValues(String pattern, String url) {
        return Optional.ofNullable(parse(pattern)
                .matchAndExtract(toPathContainer(url)))
                .map(PathPattern.PathMatchInfo::getUriVariables)
                .orElse(Collections.emptyMap());
    }
}
