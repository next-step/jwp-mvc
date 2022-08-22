package core.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterImpl {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterImpl() {
    }

    public void initHandlerAdapters() {
        handlerAdapters.add(new ForwardControllerAdapter());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("매칭되는 handler adapter를 찾지 못했습니다. handler = " + handler);
    }
}
