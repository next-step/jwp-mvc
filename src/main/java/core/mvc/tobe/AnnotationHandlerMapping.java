package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AnnotationHandlerMapping {

    private Object[] basePackage;
    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ComponentScanner componentScanner = ComponentScanner.of(basePackage);
        Map<Class<?>, Object> scannedClasses = componentScanner.doScan(Controller.class);

        mappingRegistry.register(scannedClasses);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return mappingRegistry.getHandlerExecution(new HandlerKey(requestUri, rm));
    }
}
