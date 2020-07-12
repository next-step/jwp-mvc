package core.mvc.asis;

import com.google.common.collect.Lists;
import core.mvc.HandlerAdapter;
import core.mvc.exception.NoAdapterFoundException;
import core.mvc.exception.NoHandlerFoundException;

import java.util.Arrays;
import java.util.List;

public class HandlerAdapters {
    private final List<HandlerAdapter> handlerAdapters = Lists.newArrayList();

    public HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters.addAll(Arrays.asList(handlerAdapters));
    }

    public HandlerAdapter getHandlerAdapter(Object handler) throws NoAdapterFoundException {
        return this.handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoAdapterFoundException("no adapter for handler."));
    }

}
