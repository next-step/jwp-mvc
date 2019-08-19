package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.tobe.argumentresolver.HttpServletObjectArgumentResolver;
import core.mvc.tobe.argumentresolver.MethodArgumentHandler;
import core.mvc.tobe.argumentresolver.NumberArgumentResolver;
import core.mvc.tobe.argumentresolver.UserArgumentResolver;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String METHOD_INFO_FORMAT = "%s.%s";
    private static final String MAPPING_INFO_LOG_FORMAT = "{}, execution method: {}";

    private Object[] basePackage;
    private HandlerExecutionsManager handlerExecutionsManager = new HandlerExecutionsManager();
    private MethodArgumentHandler methodArgumentHandler = new MethodArgumentHandler();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        initHandlerExecution(controllerScanner.getInstantiateControllerMap());
        methodArgumentHandlerInit();
    }

    private void methodArgumentHandlerInit() {
        methodArgumentHandler.add(new NumberArgumentResolver());
        methodArgumentHandler.add(new UserArgumentResolver());
        methodArgumentHandler.add(new HttpServletObjectArgumentResolver());
    }

    private void initHandlerExecution(Map<Class<?>, Object> instantiateControllerMap) {
        for (Class<?> aClass : instantiateControllerMap.keySet()) {
            Object controller = instantiateControllerMap.get(aClass);
            addExecutionOfController(controller, aClass);
        }
    }

    private void addExecutionOfController(Object controller, Class<?> aClass) {
        Class<RequestMapping> requestMappingClass = RequestMapping.class;
        Set<Method> methods = ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(requestMappingClass));

        for (Method method : methods) {
            HandlerKey handlerKey = createHandlerKey(method.getAnnotation(requestMappingClass));
            HandlerExecution handlerExecution = getHandlerExecution(controller, method);
            handlerExecutionsManager.put(handlerKey, handlerExecution);
            loggingMappingInfo(handlerKey, method);
        }
    }

    private HandlerExecution getHandlerExecution(Object controller, Method method) {
        return (request, response) -> {
            Object[] values = methodArgumentHandler.getValues(method, request, response);
            return (ModelAndView) method.invoke(controller, values);
        };
    }

    private HandlerKey createHandlerKey(RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    private void loggingMappingInfo(HandlerKey handlerKey, Method method) {
        String methodInfo = String.format(METHOD_INFO_FORMAT, method.getDeclaringClass().getName(), method.getName());
        logger.info(MAPPING_INFO_LOG_FORMAT, handlerKey, methodInfo);
    }

    public Set<HandlerKey> getHandlerKeys() {
        return this.handlerExecutionsManager.keySet();
    }

    public boolean isHandlerKeyPresent(String url, RequestMethod method) {
        return this.handlerExecutionsManager.containsKey(new HandlerKey(url, method));
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutionsManager.get(new HandlerKey(requestUri, requestMethod));
    }
}
