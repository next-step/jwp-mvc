package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Objects;

public class HandlerKey {

    private static final PathPatternParser PATTERN_PARSER = new PathPatternParser();

    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public boolean matches(HandlerKey handlerKey) {
        if (handlerKey == null) {
            return false;
        }
        if (equals(handlerKey)) {
            return true;
        }
        return matchesUrl(handlerKey.url)
                && matchesMethod(handlerKey.requestMethod);
    }

    private boolean matchesUrl(String url) {
        return PATTERN_PARSER.parse(url)
                .matches(PathContainer.parsePath(url));
    }

    private boolean matchesMethod(RequestMethod requestMethod) {
        return this.requestMethod == requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod;
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + requestMethod +
                '}';
    }
}
