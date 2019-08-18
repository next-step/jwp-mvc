package core.mvc.handler;

import com.google.common.collect.ImmutableList;
import core.mvc.mapping.MappingRegistry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerExecutor {

    private final MappingRegistry mappingRegistry;
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerExecutor(MappingRegistry mappingRegistry) {
        this.mappingRegistry = mappingRegistry;
        this.handlerAdapters = initialize();
    }

    private List<HandlerAdapter> initialize() {
        return ImmutableList.of(
                new ControllerHandlerAdapter(),
                new HandlerExecutionAdapter()
        );
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = mappingRegistry.getHandler(request);

        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        handlerAdapter.handle(handler, request, response);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return this.handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 handler 입니다. handler : [" + handler.getClass() + "]"));
    }

}
