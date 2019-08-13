package core.mvc.tobe;

import core.mvc.Environment;
import core.mvc.asis.RequestMapping;
import core.mvc.asis.RequestMappingHandlerAdapter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterManager {

    private List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterManager(Environment environment) {
        this.handlerAdapters = createDefaultHandlerAdapters(environment);
    }

    private List<HandlerAdapter> createDefaultHandlerAdapters(Environment environment) {
        String basePackage = environment.getProperty("component.basepackage");
        List<HandlerAdapter> handlerAdapters = new ArrayList<>();

        if (StringUtils.isNotEmpty(basePackage)) {
            handlerAdapters.add(new AnnotationHandlerMappingAdapter(new AnnotationHandlerMapping(basePackage)));
        }

        handlerAdapters.add(new RequestMappingHandlerAdapter(new RequestMapping()));

        return handlerAdapters;
    }

    public HandlerAdapter getHandler(HttpServletRequest req) throws ServletException {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(req)) {
                return handlerAdapter;
            }
        }

        throw new ServletException("No Adapter for processing servlet");
    }
}
