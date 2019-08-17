package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class HandlerAdapters {
    private List<HandlerAdapter> handlerAdapters;

    private HandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters = Arrays.asList(handlerAdapters);
    }

    public static HandlerAdapters of() {
        return new HandlerAdapters(
                new LegacyHandlerAdapter()
                , new AnnotationHandlerAdapter()
        );
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        handlerAdapter.handle(req, resp, handler);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
