package core.mvc.asis;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;

import javax.servlet.http.HttpServletRequest;

public class HandlerMapping {

    private static final Object[] ANNOTATION_BASE_PACKAGE = {"next"};

    private final RequestMapping requestMapping = new RequestMapping();
    private final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(ANNOTATION_BASE_PACKAGE);

    public void init() {
        this.requestMapping.initMapping();
        this.annotationHandlerMapping.initialize();
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        Controller controller = requestMapping.findController(request.getRequestURI());
        if (controller != null) {
            return new ControllerAdapter(controller);
        }

        return annotationHandlerMapping.getHandler(request);
    }
}
