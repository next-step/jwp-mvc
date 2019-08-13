package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.tobe.exceptions.AnnotationRequestMappingCreationException;
import core.mvc.tobe.exceptions.HandlerKeyDuplicationException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private static final int MIN_REQUEST_METHOD_NUMBER = 0;
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClass.forEach(this::mapController);
    }

    private void mapController(Class<?> aClass) {
        try {
            Object controller = aClass.getConstructor().newInstance();
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

        RequestMethod[] requestMethods = requestMapping.method();
        String url = rootPath + requestMapping.value();

        if(requestMethods.length == MIN_REQUEST_METHOD_NUMBER) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            putHandlerMapping(controller, method, url, requestMethod);
        }
    }

    private void putHandlerMapping(Object controller, Method method, String url, RequestMethod requestMethod) {
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        HandlerExecution handlerExecution = (request, response) -> (ModelAndView) method.invoke(controller, request, response);

        if (handlerExecutions.containsKey(handlerKey)) {
            throw new HandlerKeyDuplicationException(handlerKey);
        }

        logger.info("request mapping, path : {}, method : {}", handlerKey.getUrl(), handlerKey.getRequestMethod());
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
