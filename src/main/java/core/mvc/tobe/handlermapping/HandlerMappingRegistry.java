package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handlermapping.custom.AnnotationHandlerMapping2;
import core.mvc.tobe.handlermapping.custom.UrlHandlerMapping;

public class HandlerMappingRegistry {
    private static final String BASE_PACKAGE_FOR_COMPONENT_SCAN = "core.mvc";

    public static void register(){
        HandlerMappings.addHandlerMapping(new AnnotationHandlerMapping2(BASE_PACKAGE_FOR_COMPONENT_SCAN));
        HandlerMappings.addHandlerMapping(new UrlHandlerMapping());
    }
}
