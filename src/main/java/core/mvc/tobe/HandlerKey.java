package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Objects;

public class HandlerKey {

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    static {
        PATH_PATTERN_PARSER.setMatchOptionalTrailingSeparator(true);
    }

    private final String uri;
    private final PathPattern pathPattern;
    private final RequestMethod requestMethod;

    public HandlerKey(String uri, RequestMethod requestMethod) {
        this.uri = uri;
        this.requestMethod = requestMethod;
        this.pathPattern = PATH_PATTERN_PARSER.parse(uri);
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + uri + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, requestMethod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return pathPattern.matches(toPathContainer(that.uri)) &&
                requestMethod == that.requestMethod;
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
