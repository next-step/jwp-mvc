package core.mvc.tobe;

import core.mvc.Handler;
import core.mvc.asis.LegacyHandlerAdapter;
import core.mvc.asis.LegacyHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {
    private List<Handler> handlerAdapters = new ArrayList<>();

    public HandlerAdapters(Object... basePackages) {
        handlerAdapters.add(new LegacyHandlerAdapter(new LegacyHandlerMapping()));
        handlerAdapters.add(new AnnotationHandlerAdapter(new AnnotationHandlerMapping(basePackages)));
    }

    public Handler getHandler(HttpServletRequest req, HttpServletResponse resp) {
        for (Handler handler : handlerAdapters) {
            if (handler.isSupport(req)) {
                return handler;
            }
        }
        return null;
    }
}
