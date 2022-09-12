package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPatternParser;

public class HandlerKey {
    private String url;
    private RequestMethod requestMethod;
    private static final PathPatternParser parser = new PathPatternParser();

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public boolean isMatch(HandlerKey handlerKey) {
        if (!requestMethod.equals(handlerKey.requestMethod)) {
            return false;
        }
        return parser.parse(url).matches(PathContainer.parsePath(handlerKey.url));
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestMethod == null) ? 0 : requestMethod.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
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
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
}
