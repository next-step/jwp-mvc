package core.mvc;

import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappings {
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void add(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public HandlerExecution getHandler(HttpServletRequest req){
        return handlerMappings.stream()
                .map(h -> h.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("핸들러를 찾을 수 없습니다."));
    }
}
