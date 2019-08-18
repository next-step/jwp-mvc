package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class HandlerMappings {
    private Set<HandlerMapping> handlerMappings;

    public HandlerMappings(Set<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    public Object get(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("요청에 대한 핸들러를 찾을 수 없습니다."));
    }
}
