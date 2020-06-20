package core.mvc.tobe;

import core.annotation.web.RequestMethod;

import java.util.Arrays;
import java.util.Objects;

public class HandlerKey {
    private String url;
    private RequestMethod[] requestMethods;

    public HandlerKey(String url, RequestMethod[] requestMethods) {
        this.url = url;
        this.requestMethods = requestMethods;
    }

    public boolean isMatchKey(String url, RequestMethod requestMethod) {
        return equalsUrl(url) && contains(requestMethod);
    }

    private boolean equalsUrl(String url) {
        return url.equals(this.url);
    }

    private boolean contains(RequestMethod requestMethod) {
        return Arrays.stream(requestMethods)
                .anyMatch(r -> r == requestMethod);
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + Arrays.toString(requestMethods) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) &&
                Arrays.equals(requestMethods, that.requestMethods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url);
        result = 31 * result + Arrays.hashCode(requestMethods);
        return result;
    }

}
