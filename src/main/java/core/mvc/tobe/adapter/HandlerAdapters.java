package core.mvc.tobe.adapter;

import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter findHandlerAdapter(Object handler) throws NotFoundException {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }

        throw new NotExistAdapterException("요청에 맞는 어뎁터가 없습니다.");
    }

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return handlerAdapters;
    }
}
