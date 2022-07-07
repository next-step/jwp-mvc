package core.mvc.tobe;

import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackages;

    private HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void initialize() {
        for (Object basePackage : basePackages) {
            ControllerScanner controllerScanner = new ControllerScanner((String) basePackage);
            Map<Class, Object> controllers = controllerScanner.getControllers();

            handlerExecutions = HandlerExecutions.of(controllers);
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod()
                                                        .toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
