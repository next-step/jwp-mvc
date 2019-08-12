package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerMappingAdapter implements HandlerAdapter {

    private AnnotationHandlerMapping annotationHandlerMapping;

    public AnnotationHandlerMappingAdapter(AnnotationHandlerMapping annotationHandlerMapping) {
        this.annotationHandlerMapping = annotationHandlerMapping;
        annotationHandlerMapping.initialize();
    }

    @Override
    public boolean supports(HttpServletRequest req) {
        return annotationHandlerMapping.hasHandler(req);
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecution handler = annotationHandlerMapping.getHandler(req);
        Object result = handler.handle(req, resp);
        ResultValueHandler.execute(result, req, resp);
    }
}
