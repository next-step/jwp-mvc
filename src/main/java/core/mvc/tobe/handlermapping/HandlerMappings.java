package core.mvc.tobe.handlermapping;

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
}
