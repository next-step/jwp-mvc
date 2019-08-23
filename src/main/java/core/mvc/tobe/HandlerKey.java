package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerKey {
    private static final int MIN_REQUEST_METHOD_NUMBER = 0;

    private String url;
    private RequestMethod requestMethod;

    public static List<HandlerKey> createByRequestMapping(RequestMapping requestMapping, String path) {
        String url = path + requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == MIN_REQUEST_METHOD_NUMBER) {
            requestMethods = RequestMethod.values();
        }

        return Arrays.stream(requestMethods)
                .map(it -> new HandlerKey(url, it))
                .collect(Collectors.toList());
    }

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
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
