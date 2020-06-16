package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class HandlerKey {
    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    private final String url;
    private final PathPattern pathPattern;
    private final RequestMethod requestMethod;

    static {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
    }

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        if (Objects.isNull(url) || Objects.isNull(requestMethod)) {
            throw new IllegalArgumentException("Fail to create HandlerKey cuz there is null argument");
        }

        this.url = url;
        this.pathPattern = PATH_PATTERN_PARSER.parse(url);
        this.requestMethod = requestMethod;
    }

    public static HandlerKey from(final HttpServletRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Fail to create HandlerKey cuz HttpRequest is null");
        }

        return new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase()));
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }

        return PathContainer.parsePath(path);
    }

    public Map<String, String> parseRequestParam(String requestURI) {
        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(toPathContainer(requestURI));

        if (pathMatchInfo == null) {
            return Collections.emptyMap();
        }

        return pathMatchInfo.getUriVariables();
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestMethod); // url이 다 다른데 hash값 처리를 어떻게 해야지 좋을까
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return that.pathPattern.matches(toPathContainer(this.url)) &&
                requestMethod == that.requestMethod;
    }
}
