package core.mvc;

import core.mvc.asis.Controller;
import core.mvc.asis.LegacyHandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class HandlerMappings {

    private List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        this.handlerMappings = new ArrayList<>();
    }

    public void add(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public HandlerMapping getHandlerMapping(Controller controller) {
        if (controller != null) {
            return handlerMappings.stream()
                    .filter(handlerMapping -> handlerMapping instanceof LegacyHandlerMapping)
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new);
        }
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping instanceof AnnotationHandlerMapping)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
