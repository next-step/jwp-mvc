package core.mvc.tobe;

import core.annotation.web.RequestMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerKey {
    private String url;
    private RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static List<HandlerKey> allMethodsKey(String url) {
        return toList(url, RequestMethod.values());
    }

    public static List<HandlerKey> listOf(String url, RequestMethod[] requestMethods) {
        return toList(url, requestMethods);
    }

    private static List<HandlerKey> toList(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                     .map(requestMethod -> new HandlerKey(url, requestMethod))
                     .collect(Collectors.toList());
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
