package core.mvc;

import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By kjs4395 on 2020-06-18
 */
public class ControllerExecutor {
    private final RequestMapping requestMapping;
    private final AnnotationHandlerMapping annotationHandlerMapping;

    public ControllerExecutor(RequestMapping requestMapping, AnnotationHandlerMapping annotationHandlerMapping) {
        this.requestMapping = requestMapping;
        this.annotationHandlerMapping = annotationHandlerMapping;
    }

    public Object findExecutor(HttpServletRequest request) {
        if(annotationHandlerMapping.containsExecution(request)) {
            return annotationHandlerMapping.getHandler(request);
        }

        return requestMapping.findController(request.getRequestURI());
    }
}
