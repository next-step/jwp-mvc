package core.mvc.tobe;

import core.mvc.asis.LegacyMvcHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class HandlerMappings {
    private List<HandlerMapping> handlerMappings;

    private HandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.asList(handlerMappings);
    }

    public static HandlerMappings of() {
        return new HandlerMappings(initLegacyMvcHandlerMapping()
                , initAnnotationHandlerMapping());
    }

    private static LegacyMvcHandlerMapping initLegacyMvcHandlerMapping() {
        LegacyMvcHandlerMapping lmhm = new LegacyMvcHandlerMapping();
        lmhm.initialize();
        return lmhm;
    }

    private static AnnotationHandlerMapping initAnnotationHandlerMapping() {
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
        return ahm;
    }

    public boolean support(HttpServletRequest req) {
        return handlerMappings.stream()
                .anyMatch(handlerMapping -> handlerMapping.support(req));
    }

    public Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(req))
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .findFirst()
                .orElseThrow(() -> new IllegalAccessError("비정상 접근입니다."));
    }
}
