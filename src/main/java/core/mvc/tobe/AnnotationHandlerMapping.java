package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.exception.ReflectionsException;
import org.slf4j.Logger;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final ControllerScanner controllerScanner;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) throws ReflectionsException {
        this.basePackage = basePackage;
        this.controllerScanner = new ControllerScanner(this.basePackage);

        this.initialize();
    }

    public void initialize() throws ReflectionsException {
        this.handlerExecutions.putAll(this.controllerScanner.scan());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
