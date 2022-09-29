package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(String... basePackage) {
        this.basePackage = basePackage;
        initialize();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        handlerExecutions.putAll(controllerScanner.getHandlerExecutions());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        logger.debug("Request URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase())));
    }
}
