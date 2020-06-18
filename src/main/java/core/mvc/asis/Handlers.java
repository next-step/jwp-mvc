package core.mvc.asis;

import com.google.common.collect.Maps;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.handler.ControllerHandler;
import core.mvc.tobe.handler.Handler;
import core.mvc.tobe.handler.HandlerExecutionHandler;

import java.util.Map;

public class Handlers {
    private Map<Class<?>, Handler> handlers = initHandlers();

    public Map<Class<?>, Handler> initHandlers() {
        Map<Class<?>, Handler> handlerMap = Maps.newHashMap();
        handlerMap.put(Controller.class, new ControllerHandler());
        handlerMap.put(HandlerExecution.class, new HandlerExecutionHandler());

        return handlerMap;
    }

    public Handler getHandler(Object handlerObj) {
        return handlers.entrySet()
            .stream()
            .filter(entry -> entry.getKey().isInstance(handlerObj))
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(null);
    }
}
