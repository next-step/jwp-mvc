package core.mvc.tobe;

import core.annotation.web.RequestMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HandlerKey {
    private String[] url;
    private RequestMethod[] requestMethod;

    public HandlerKey(String[] url, RequestMethod[] requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public boolean containsMethod(String method) {
        return Arrays.stream(requestMethod)
                .anyMatch(requestMethod -> requestMethod.name().equalsIgnoreCase(method));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{url=[");
        sb.append(String.join(", ", url));
        sb.append("], requestMethod=[");
        sb.append(Arrays.stream(requestMethod).map(Enum::name).collect(Collectors.joining(", ")));
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestMethod == null) ? 0 : Arrays.hashCode(requestMethod));
        result = prime * result + ((url == null) ? 0 : Arrays.hashCode(url));
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
        if (!Arrays.equals(requestMethod, other.requestMethod))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else {
            return Arrays.equals(url, other.url);
        }
        return true;
    }
}
