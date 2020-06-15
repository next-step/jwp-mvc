package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.tobe.resolver.PathPatternUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
        return Objects.equals(url, that.getUrl()) && requestMethod == that.requestMethod;
    }

    public boolean matchesPathPattern(HandlerKey that) {
        return PathPatternUtil.parse(this.url)
                    .matches(PathPatternUtil.toPathContainer(that.getUrl()));
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
}
