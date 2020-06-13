package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import lombok.Getter;
import next.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

@Getter
public class RequestMappingMethod {
    private Method method;
    private Object instance;
    private String url;
    private Set<RequestMethod> requestMethods;

    public RequestMappingMethod(Method method, Object instance, String url, Set<RequestMethod> requestMethods) {
        if (isInvalidArguments(method, instance, url, requestMethods)) {
            throw new IllegalArgumentException();
        }

        this.method = method;
        this.instance = instance;
        this.url = url;
        this.requestMethods = requestMethods;
    }

    private boolean isInvalidArguments(Method method, Object instance, String uri, Set<RequestMethod> requestMethods) {
        return Objects.isNull(method) ||
            Objects.isNull(instance) ||
            StringUtils.isEmpty(uri) ||
            isInvalidRequestMethods(requestMethods);
    }

    private boolean isInvalidRequestMethods(Set<RequestMethod> requestMethods) {
        return Objects.isNull(requestMethods) || requestMethods.size() <= 0;
    }
}
