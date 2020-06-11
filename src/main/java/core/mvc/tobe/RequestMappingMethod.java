package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Set;

@Getter
public class RequestMappingMethod {
    private Method method;
    private Object instance;
    private String uri;
    private RequestMethod[] requestMethod;

    public RequestMappingMethod(Method method, Object instance, String uri, Set<RequestMethod> requestMethods) {
        this.method = method;
        this.instance = instance;
        this.uri = uri;
        this.requestMethod = (RequestMethod[]) requestMethods.toArray();
    }
}
