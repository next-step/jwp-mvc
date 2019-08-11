package core.mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MappingRegistry {

    private final List<Mapping> mappings;

    public MappingRegistry(Mapping ...mapping) {
        mappings = Arrays.asList(mapping);
        initialize();
    }

    private void initialize() {
        mappings.forEach(Mapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return this.mappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 URI 입니다. URI : ["+ request.getRequestURI() +"]"));
    }
}
