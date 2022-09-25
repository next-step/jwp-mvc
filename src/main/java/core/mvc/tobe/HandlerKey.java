package core.mvc.tobe;

import core.annotation.web.RequestMethod;

import java.util.regex.Pattern;

public class HandlerKey {
    private String url;
    private RequestMethod requestMethod;

    public HandlerKey(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public boolean isMatched(RequestMethod requestMethod, String requestUri) {
        return (isSameMethod(requestMethod) || isSameMethod(RequestMethod.ALL)) && isMatchedUri(requestUri);
    }
    private boolean isSameMethod(RequestMethod requestMethod) {
        return this.requestMethod == requestMethod;
    }

    private boolean isMatchedUri(String requestUri) {
        Pattern pattern = Pattern.compile(url.replaceAll("\\{[^/]+}", "[^/]+"));
        return pattern.matcher(requestUri).matches();
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
