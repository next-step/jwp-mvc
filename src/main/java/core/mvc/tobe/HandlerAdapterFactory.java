package core.mvc.tobe;

import core.mvc.Environment;
import core.mvc.asis.RequestMapping;
import core.mvc.asis.RequestMappingHandlerAdapter;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class HandlerAdapterFactory {

    private List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterFactory(Environment environment) {
        this.handlerAdapters = createDefaultHandlerAdapters(environment);
    }

    private List<HandlerAdapter> createDefaultHandlerAdapters(Environment environment) {
        String basePackage = environment.getProperty("component.basepackage");
        return asList(
                new AnnotationHandlerMappingAdapter(basePackage),
                new RequestMappingHandlerAdapter(new RequestMapping())
        );
    }

    public HandlerAdapter getHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(request)) {
                return handlerAdapter;
            }
        }

        return null;
    }

    public void destroy() {
        handlerAdapters.clear();
    }
}
