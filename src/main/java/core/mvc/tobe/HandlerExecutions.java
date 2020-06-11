package core.mvc.tobe;

import java.util.Map;

public class HandlerExecutions {
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public HandlerExecutions(Map<HandlerKey, HandlerExecution> handlerExecutions) {
        this.handlerExecutions = handlerExecutions;
    }
/*
    public HandlerExecution get(HandlerKey handlerKey) {
        return handlerExecutions.getOrDefault(handlerKey, getDefault(handlerKey));

    }

    private HandlerExecution getDefault(HandlerKey handlerKey) {

    }*/
}
