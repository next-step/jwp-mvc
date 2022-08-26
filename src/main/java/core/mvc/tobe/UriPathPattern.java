package core.mvc.tobe;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;
import java.util.Map;

public class UriPathPattern {

    private final PathPattern pathPattern;

    public UriPathPattern(String patternPath) {
        this.pathPattern = parse(patternPath);
    }

    public boolean matched(String requestURI) {
        return pathPattern.matches(toPathContainer(requestURI));
    }

    public Map<String, String> matchAndExtract(String requestURI) {
        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(toPathContainer(requestURI));
        if (pathMatchInfo == null) {
            return Collections.emptyMap();
        }
        return pathMatchInfo.getUriVariables();
    }

    private PathPattern parse(String patternPath) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(patternPath);
    }

    private PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
