package core.mvc.tobe.handler;

import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

public class HandlerKey {
    private String url;
    private RequestMethod[] requestMethod;

    public HandlerKey(String url, RequestMethod... requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public static HandlerKey of(HttpServletRequest request){
        String requestUri = request.getRequestURI();

        if(request.getMethod() == null){
            return new HandlerKey(requestUri, RequestMethod.values());
        }
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return new HandlerKey(requestUri, method);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return Objects.equals(url, that.url) &&
                Arrays.equals(requestMethod, that.requestMethod);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url);
        result = 31 * result + Arrays.hashCode(requestMethod);
        return result;
    }

    @Override
    public String toString() {
        return "HandlerKey{" +
                "url='" + url + '\'' +
                ", requestMethod=" + Arrays.toString(requestMethod) +
                '}';
    }
}
