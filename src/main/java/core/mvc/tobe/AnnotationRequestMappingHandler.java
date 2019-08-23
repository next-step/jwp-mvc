package core.mvc.tobe;

import core.mvc.HandlerExecutor;
import core.mvc.RequestMappingHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hspark on 2019-08-16.
 */
public class AnnotationRequestMappingHandler implements RequestMappingHandler {
    private final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();

    @Override
    public void initialize() {
        annotationHandlerMapping.initialize();
    }

    @Override
    public Optional<HandlerExecutor> getHandlerExecutor(HttpServletRequest httpServletRequest) {
        HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(httpServletRequest);
        if (Objects.isNull(handlerExecution)) {
            return Optional.empty();
        }

        HandlerExecutor handlerExecutor = (request, response) -> handlerExecution.handle(request, response);

        return Optional.of(handlerExecutor);
    }
}
