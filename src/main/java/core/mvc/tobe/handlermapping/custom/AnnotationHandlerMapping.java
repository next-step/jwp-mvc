package core.mvc.tobe.handlermapping.custom;

import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handler.HandlerKey;
import core.mvc.tobe.handlermapping.ControllerScanner;
import core.mvc.tobe.handlermapping.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object basePackage;
    private HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public HandlerExecutions init() {
        this.handlerExecutions = executeComponentScan();
        return handlerExecutions;
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return findHandler(request) != null;
    }

    @Override
    public HandlerExecution findHandler(HttpServletRequest request) {
        HandlerExecution handler = handlerExecutions.getValueByKey(HandlerKey.of(request));

        if(Objects.isNull(handler)){
            return null;
        }

        return handler;
    }

    private HandlerExecutions executeComponentScan() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        return controllerScanner.scan();
    }
}
