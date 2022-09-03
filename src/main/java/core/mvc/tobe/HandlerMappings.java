package core.mvc.tobe;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

public class HandlerMappings {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappings(List<HandlerMapping> handlerMappings) {
        handlerMappings.forEach(HandlerMapping::initialize);

        this.handlerMappings = handlerMappings;
    }

    public Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
            .map(it -> it.getHandler(req))
            .filter(Objects::nonNull)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 URL 입니디. url" + req.getRequestURI()));
    }
}
