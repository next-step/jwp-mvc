package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final String PACKAGE_OF_SCAN = "next.controller";
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public static AnnotationHandlerMapping of() {
        return new AnnotationHandlerMapping(PACKAGE_OF_SCAN);
    }

    public void initialize() {
        logger.debug("basePackage : {}", basePackage);
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(this::initController);
        logger.debug("handlerExecutions : {}", handlerExecutions);
    }

    private void initController(Class<?> controller) {
        Object controllerInstance;
        try {
            controllerInstance = controller.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InitializedException(e);
        }

        Arrays.stream(controller.getMethods())
                .forEach(method -> initMethod(controllerInstance, method));
    }

    private void initMethod(Object controllerInstance, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            initHandler(controllerInstance, method, annotation);
        }
    }

    private void initHandler(Object controllerInstance, Method method, RequestMapping annotation) {
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        if (!isExistHandler(request)) {
            throw new NotFoundServletException();
        }
        HandlerExecution handler = getHandler(request);
        return handler.handle(request, response);
    }

    @Override
    public boolean isExistHandler(HttpServletRequest request) {
        HandlerExecution handler = getHandler(request);
        return null != handler;
    }

    private HandlerExecution getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}
