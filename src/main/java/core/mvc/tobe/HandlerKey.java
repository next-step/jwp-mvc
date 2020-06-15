package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import next.util.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Objects;

@Getter
@Slf4j
public class HandlerKey {
    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return matchesUrl(that) && requestMethod == that.requestMethod;
    }

    private boolean matchesUrl(HandlerKey that) {
        log.debug("this: {}", StringUtils.toPrettyJson(this));
        log.debug("that: {}", StringUtils.toPrettyJson(that));

        return Objects.equals(url, that.getUrl()) ||
            parse(this.url).matches(toPathContainer(that.getUrl()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
            "url='" + url + '\'' +
            ", requestMethod=" + requestMethod +
            '}';
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
