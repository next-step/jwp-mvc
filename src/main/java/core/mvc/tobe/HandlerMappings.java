package core.mvc.tobe;

import core.mvc.asis.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private static Logger logger = LoggerFactory.getLogger(HandlerMappings.class);
    private List<HandlerMapping> mappings;

    public HandlerMappings(HandlerMapping... mapping) {
        this.mappings = List.of(mapping);
        initialize();
    }

    public List<HandlerMapping> getMappings() {
        return Collections.unmodifiableList(mappings);
    }

    public Controller findController(HttpServletRequest request) {
        return mappings.stream()
                .map(it -> it.getHandler(request))
                .map(Controller.class::cast)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }
}
