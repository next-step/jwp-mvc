package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        if (handlerMappings.isEmpty()) {
            throw new IllegalArgumentException("HandlerMapping이 존재하지 않습니다.");
        }
        this.handlerMappings = handlerMappings;
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                              .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(req)))
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("지원하지 않은 Handler type 입니다."))
                              .getHandler(req);
    }
}
