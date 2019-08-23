package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.tobe.exceptions.AnnotationRequestMappingCreationException;
import core.mvc.tobe.exceptions.HandlerKeyDuplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllerMap = controllerScanner.getControllers();
        controllerMap.entrySet().forEach(it -> mapController(it.getKey(), it.getValue()));
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void mapController(Class<?> aClass, Object controller) {
        try {
            String rootPath = aClass.getAnnotation(Controller.class).path();

            Arrays.stream(aClass.getDeclaredMethods())
                    .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                    .forEach(it -> mapMethod(controller, rootPath, it));

        } catch (Exception e) {
            throw new AnnotationRequestMappingCreationException(e);
        }
    }

    private void mapMethod(Object controller, String rootPath, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = HandlerKey.createByRequestMapping(requestMapping, rootPath);
        handlerKeys.forEach(it -> putHandlerMapping(it, controller, method));
    }


    private void putHandlerMapping(HandlerKey handlerKey, Object controller, Method method) {
        HandlerExecution handlerExecution = (request, response) -> (ModelAndView) method.invoke(controller, request, response);

        if (handlerExecutions.containsKey(handlerKey)) {
            throw new HandlerKeyDuplicationException(handlerKey);
        }

        logger.info("request mapping, path : {}, method : {}", handlerKey.getUrl(), handlerKey.getRequestMethod());
        handlerExecutions.put(handlerKey, handlerExecution);
    }
}
