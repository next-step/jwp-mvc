package core.mvc.tobe;

import core.mvc.asis.LegacyHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandlerMappings {

    private final List<HandlerMapping> mappings = new ArrayList<>();

    public HandlerMappings() {
        initHandlerMappings();
    }

    private void initHandlerMappings() {
        mappings.add(new LegacyHandlerMapping());
        mappings.add(new AnnotationHandlerMapping("core.mvc.tobe"));
        initialize();
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public Optional<Object> getHandler(HttpServletRequest request) {
        return mappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst();
    }
}
