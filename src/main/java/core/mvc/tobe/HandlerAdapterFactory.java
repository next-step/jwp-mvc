package core.mvc.tobe;

import core.mvc.asis.RequestMapping;
import core.mvc.asis.RequestMappingHandlerAdapter;

import java.util.List;

import static java.util.Arrays.asList;

public class HandlerAdapterFactory {

    public HandlerAdapter requestMappingHandlerAdapter;
    public HandlerAdapter annotationHandlerMappingAdapter;

    public HandlerAdapterFactory(Environment environment) {
        String basePackage = environment.getProperty("component.basepackage");

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);

        requestMappingHandlerAdapter = new RequestMappingHandlerAdapter(new RequestMapping());
        annotationHandlerMappingAdapter = new AnnotationHandlerMappingAdapter(annotationHandlerMapping);
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return asList(requestMappingHandlerAdapter, annotationHandlerMappingAdapter);
    }

}
