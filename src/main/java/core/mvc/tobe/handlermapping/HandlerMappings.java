package core.mvc.tobe.handlermapping;

import core.mvc.tobe.handler.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HandlerMappings {
    private static Set<HandlerMapping> handlerMappings = new HashSet<>();

    public static void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public static Set<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableSet(handlerMappings);
    }

    public static HandlerExecution findHandler(HttpServletRequest request){
        HandlerMapping handlerMapping = findHandlerMapping(request);
        return handlerMapping.findHandler(request);
    }

    private static HandlerMapping findHandlerMapping(HttpServletRequest request){
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.hasHandler(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found matched HandlerMapping"));
    }
}
