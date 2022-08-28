package next.support.resolver;

import core.mvc.tobe.HandlerKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

}
