package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import java.util.Objects;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class HandlerKey {

    private static final PathPatternParser PATH_PATTERN_PARSER = new PathPatternParser();

    private final PathPattern urlPattern;
    private final RequestMethod requestMethod;

    public HandlerKey(final String urlPattern, final RequestMethod requestMethod) {
        this(PATH_PATTERN_PARSER.parse(urlPattern), requestMethod);
    }

    public HandlerKey(final PathPattern urlPattern, final RequestMethod requestMethod) {
        this.urlPattern = urlPattern;
        this.requestMethod = requestMethod;
    }

    private boolean isMatchPattern(final String path) {
        return urlPattern.matches(toPathContainer(path));
    }

    private PathContainer toPathContainer(String path) {
        return PathContainer.parsePath(Objects.requireNonNullElse(path, ""));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HandlerKey that = (HandlerKey) o;
        return isMatchPattern(that.urlPattern.getPatternString()) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlPattern, requestMethod);
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + urlPattern + ", requestMethod=" + requestMethod + "]";
    }
}
