package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MappingRegistry {

    private final List<HandlerMapping> handlerMappings;

    public MappingRegistry(HandlerMapping... handlerMapping) {
        this(Arrays.asList(handlerMapping));
    }

    public MappingRegistry(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
        initialize();
    }

    private void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return this.handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 URI 입니다. URI : [" + request.getRequestURI() + "]"));
    }
}
