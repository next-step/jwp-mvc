package core.mvc.tobe;

import java.util.Map;

public interface AnnotationHandler {

    void init();

    Map<HandlerKey, HandlerExecution> getExecutionMap();
}
