package core.mvc.tobe;

import java.util.List;
import java.util.Objects;

import core.annotation.web.RequestMethod;

public class HandlerKey {
    private final String url;
    private final RequestMethod requestMethod;
    private final List<String> parameterNames;

    public HandlerKey(String url, RequestMethod requestMethod, List<String> parameterNames) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.parameterNames = parameterNames;
    }

    @Override
    public String toString() {
        return "HandlerKey [url=" + url + ", requestMethod=" + requestMethod + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) && requestMethod == that.requestMethod && parameterNames.equals(that.parameterNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod, parameterNames);
    }
}
