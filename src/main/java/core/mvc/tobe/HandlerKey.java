package core.mvc.tobe;

import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class HandlerKey {
    private final String url;
    private final RequestMethod requestMethod;

    public HandlerKey(final String url, final RequestMethod requestMethod) {
        if (Objects.isNull(url) || Objects.isNull(requestMethod)) {
            throw new IllegalArgumentException("Fail to create HandlerKey cuz there is null argument");
        }

        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey from(final HttpServletRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("Fail to create HandlerKey cuz HttpRequest is null");
        }

        return new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase()));
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
