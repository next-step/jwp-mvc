package core.mvc.tobe;

import core.annotation.web.RequestMethod;
import core.mvc.asis.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

public class HandlerMappings {
    private AnnotationHandlerMapping annotationHandlerMapping;
    private RequestMapping legacyHandlerMapping;

    public void init() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        legacyHandlerMapping = new RequestMapping();
        legacyHandlerMapping.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestUri, httpMethod);

        if(annotationHandlerMapping.existHandler(handlerKey)) {
            return annotationHandlerMapping.getHandler(request);
        }

        if(legacyHandlerMapping.existController(requestUri)) {
           return legacyHandlerMapping.findController(requestUri);
        }

        throw new IllegalArgumentException("not matched any controller");
    }

}
