package core.mvc.tobe;

import core.mvc.asis.RequestMapping;
import core.mvc.asis.RequestMappingHandlerAdapter;
import core.mvc.tobe.support.*;

import java.util.List;

import static java.util.Arrays.asList;

public class HandlerAdapterFactory {

    public HandlerAdapter requestMappingHandlerAdapter;

    public HandlerAdapter annotationHandlerMappingAdapter;

    public HandlerAdapterFactory(Environment environment) {
        String basePackage = environment.getProperty("component.basepackage");

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.setArgumentResolvers(getArgumentResolvers());

        requestMappingHandlerAdapter = new RequestMappingHandlerAdapter(new RequestMapping());
        annotationHandlerMappingAdapter = new AnnotationHandlerMappingAdapter(annotationHandlerMapping);
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return asList(requestMappingHandlerAdapter, annotationHandlerMappingAdapter);
    }

    public List<ArgumentResolver> getArgumentResolvers() {
        return asList(
                new HttpRequestArgumentResolver(),
                new HttpResponseArgumentResolver(),
                new RequestParamArgumentResolver()
        );
    }

}
