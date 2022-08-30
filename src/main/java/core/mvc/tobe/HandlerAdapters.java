package core.mvc.tobe;

import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapters;

    public HandlerAdapters(List<HandlerAdapter> adapters) {
        this.adapters = adapters;
    }

    public HandlerAdapter findAdapter(Object handler, String requestUri) {
        return adapters.stream()
            .filter(it -> it.supports(handler))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 핸들러입니다. url" + requestUri));
    }
}
