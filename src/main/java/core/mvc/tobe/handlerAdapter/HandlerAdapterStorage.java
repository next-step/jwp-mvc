package core.mvc.tobe.handlerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterStorage {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterStorage() {
    }

    public void init() {
        handlerAdapters.add(new RequestMappingHandlerAdapter());
        handlerAdapters.add(new SimpleControllerHandlerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.support(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("매칭되는 HandlerAdapter를 찾지 못했습니다. handler = " + handler);
    }
}
