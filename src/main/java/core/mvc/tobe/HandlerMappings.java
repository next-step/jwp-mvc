package core.mvc.tobe;

import java.util.List;

public class HandlerMappings {

    private List<HandlerMapping> mappings;

    public HandlerMappings(List<HandlerMapping> mappings) {
        this.mappings = mappings;
    }

    public HandlerMappings(HandlerMapping... mappings) {
        this(List.of(mappings));
    }

    public List<HandlerMapping> getMappings() {
        return mappings;
    }
}
