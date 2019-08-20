package core.web.context;

import core.mvc.HandlerAdapter;
import core.mvc.HandlerMapping;
import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.tobe.RequestMappingHandlerAdapter;
import core.mvc.tobe.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.List;

public class ApplicationContext {
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public ApplicationContext(Object... basePackage) {

        handlerMappings = Arrays.asList(new RequestMapping(), new RequestMappingHandlerMapping(basePackage));
        handlerAdapters = Arrays.asList(new ControllerHandlerAdapter(), new RequestMappingHandlerAdapter());
    }

    public List<HandlerMapping> getHandlerMappings() {
        return handlerMappings;
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return handlerAdapters;
    }
}
