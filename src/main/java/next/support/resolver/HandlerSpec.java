package next.support.resolver;

import core.annotation.web.RequestMapping;
import core.mvc.tobe.HandlerKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class HandlerSpec {

    private Object handler;
    private HandlerKey handlerKey;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    public HandlerSpec(Object handler, HandlerKey handlerKey, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.handler = handler;
        this.handlerKey = handlerKey;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    public Object getHandler() {
        return handler;
    }

    public HandlerKey getHandlerKey() {
        return handlerKey;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public String getOriginHandlerUri() {
        Annotation requestMapping = Arrays.stream(((Method) this.getHandler()).getDeclaredAnnotations())
                .filter(annotation -> annotation.annotationType().equals(RequestMapping.class))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        return this.getUrl(requestMapping);
    }

    private String getUrl(Annotation requestMapping) {
        Class<?> clazz = requestMapping.annotationType();
        Method pathMethod = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals("value"))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        try {
            return String.valueOf(pathMethod.invoke(requestMapping));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
