package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Objects;

public class HandlerKey {
    private String url;
    private RequestMethod requestMethod;
    private PathPattern pathPattern;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.pathPattern = PathPatternUtil.getPathPattern(url);
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(url,requestMethod);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HandlerKey other = (HandlerKey) obj;
        if (requestMethod != other.requestMethod)
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if(pathPattern.matches(PathPatternUtil.toPathContainer(other.url))){
            return true;
        }
        return true;
    }
}
