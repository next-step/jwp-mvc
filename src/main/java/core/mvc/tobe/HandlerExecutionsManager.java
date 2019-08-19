package core.mvc.tobe;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class HandlerExecutionsManager {
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public void put(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Set<HandlerKey> keySet() {
        return handlerExecutions.keySet();
    }

    public boolean containsKey(HandlerKey handlerKey) {
        return handlerExecutions.keySet().stream()
                .anyMatch(key -> PathPatternUtils.isMatch(key.getUrl(), handlerKey.getUrl()));
    }

    public HandlerExecution get(HandlerKey handlerKey) {
        return handlerExecutions.keySet().stream()
                .filter(key -> matchHandleKey(handlerKey, key))
                .map(key -> handlerExecutions.get(key))
                .findFirst()
                .orElse(null);
    }

    private boolean matchHandleKey(HandlerKey handlerKey, HandlerKey key) {
        return PathPatternUtils.isMatch(key.getUrl(), handlerKey.getUrl()) && key.matchMethod(handlerKey.getMethod());
    }
}
