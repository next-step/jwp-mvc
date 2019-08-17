package core.mvc.tobe;

import core.mvc.asis.LegacyHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerMappings {
    private List<HandlerMapping> handlerMappings;

    private HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.asList(handlerMappings);
    }

    public static HandlerMappings of() {
        return new HandlerMappings(initLegacyHandlerMapping()
                , initAnnotationHandlerMapping());
    }

    private static LegacyHandlerMapping initLegacyHandlerMapping() {
        LegacyHandlerMapping lhm = new LegacyHandlerMapping();
        lhm.initialize();
        return lhm;
    }

    private static AnnotationHandlerMapping initAnnotationHandlerMapping() {
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
        return ahm;
    }

    public Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(req))
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
