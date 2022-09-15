package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HandlerMappings {

    private final Set<HandlerMapping> mappings = new HashSet<>();

    public void add(HandlerMapping handlerMapping) {
        mappings.add(handlerMapping);
    }

    public Object findHandler(HttpServletRequest req) throws NoSuchMethodException {
        return mappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(req)))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .orElseThrow(() -> new NoSuchMethodException("URI Resource doesn't exists."));
    }
}
