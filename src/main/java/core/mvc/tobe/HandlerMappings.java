package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HandlerMappings {
    private static List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMapping(HandlerMapping... handlerMappings) {
        this.handlerMappings.addAll(List.of(handlerMappings));
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(it -> it.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
