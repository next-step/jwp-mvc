package core.mvc;

import core.mvc.handler.HandlerExecution;
import core.mvc.support.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdapter {

    private final HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite;

    public HandlerAdapter() {
        handlerMethodArgumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        handlerMethodArgumentResolverComposite.addResolver(new ServletRequestResolver());
        handlerMethodArgumentResolverComposite.addResolver(new ServletResponseResolver());
        handlerMethodArgumentResolverComposite.addResolver(new RequestParamResolver());
        handlerMethodArgumentResolverComposite.addResolver(new ModelAttributeResolver());
        handlerMethodArgumentResolverComposite.addResolver(new PathVariableResolver());
    }

    public ModelAndView handle(HandlerExecution handler, HttpServletRequest req, HttpServletResponse resp) {
        handler.setHandlerMethodArgumentResolverComposite(handlerMethodArgumentResolverComposite);
        return handler.handle(req, resp);
    }

}
