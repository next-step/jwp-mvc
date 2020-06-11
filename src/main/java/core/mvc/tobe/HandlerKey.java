package core.mvc.tobe;

import com.google.common.collect.Sets;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class HandlerKey {
    private String url;
    private RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static Set<HandlerKey> from(RequestMapping annotation) {
        Set<HandlerKey> handlerKeys = Sets.newHashSet();

        RequestMethod[] requestMethods = annotation.method();

        if (annotation.method().length <= 0) {
            requestMethods = RequestMethod.values();
        }

        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(annotation.value(), requestMethod))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) &&
            requestMethod == that.requestMethod;
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
