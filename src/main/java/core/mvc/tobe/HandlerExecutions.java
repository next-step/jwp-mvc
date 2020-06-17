package core.mvc.tobe;

import core.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

public class HandlerExecutions {
    private final Set<HandlerExecution> handlerExecutions = new HashSet<>();

    public HandlerExecutions() {}

    public HandlerExecution getHandler(final HttpServletRequest request) {
        return handlerExecutions.stream()
                .filter(handlerExecution -> handlerExecution.isSupport(request))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("There is no match method for request"));
    }

    public void add(final HandlerExecution handlerExecution) {
        handlerExecutions.add(handlerExecution);
    }
}
