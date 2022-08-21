package core.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class HandlerMappings {
    private static final String NOT_FOUND_URL = "존재하지 않는 URL입니다.";

    private List<HandlerMapping> mappings;

    public HandlerMappings() {
        mappings = new ArrayList<>();
    }

    public void addAll(List<HandlerMapping> handlerMappings) {
        for(HandlerMapping handlerMapping : handlerMappings) {
            mappings.add(handlerMapping);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_URL));
    }

}
