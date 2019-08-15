package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final String DEFAULT_PACKAGE = "next.controller";
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public static AnnotationHandlerMapping of() {
        return new AnnotationHandlerMapping(DEFAULT_PACKAGE);
    }

    public void initialize() {
        logger.debug("basePackage : {}", basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();


        controllers.entrySet()
                .forEach(this::initMethod);
        logger.debug("handlerExecutions : {}", handlerExecutions);
    }

    private void initMethod(Map.Entry<Class<?>, Object> controller) {
        Set<Method> methods = ReflectionUtils.getAllMethods(controller.getKey(),
                ReflectionUtils.withAnnotation(RequestMapping.class));

        for (Method method : methods) {
            initHandler(controller.getValue(), method);
        }
    }

    private void initHandler(Object controllerInstance, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public boolean isExists(HttpServletRequest request) {
        return null != getHandler(request);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        if (!isExists(request)) {
            throw new NotFoundServletException();
        }
        HandlerExecution handler = getHandler(request);
        return handler.handle(request, response);
    }

    private HandlerExecution getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
