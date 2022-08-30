package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.resolver.*;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private static final List<ArgumentResolver> argumentResolvers = asList(
            new HttpRequestArgumentResolver(),
            new HttpResponseArgumentResolver(),
            new RequestParamArgumentResolver(),
            new PathVariableArgumentResolver(),
            new RequestBodyArgumentResolver(),
            new SimpleArgumentResolver()
    );

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        initializeHandlerExecution(controllerScanner.getController());
    }

    private void initializeHandlerExecution(Map<Class<?>, Object> controller) {
        Set<Method> methods = getMethods(controller);
        for (Method method : methods) {
            Class<?> declaringClass = method.getDeclaringClass();
            Controller controllerAnnotation = declaringClass.getAnnotation(Controller.class);
            addHandlerExecution(controller.get(declaringClass), controllerAnnotation.value(), method);
        }
    }

    private Set<Method> getMethods(Map<Class<?>, Object> controller) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : controller.keySet()) {
            methods.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return methods;
    }

    private void addHandlerExecution(Object controllerInstance, String controllerLevelPath, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        handlerExecutions.put(createHandlerKey(controllerLevelPath, requestMapping), new HandlerExecution(argumentResolvers, controllerInstance, method));
    }

    private HandlerKey createHandlerKey(String controllerLevelPath, RequestMapping rm) {
        logger.info("Add RequestMapping URL : {}, method : {}", controllerLevelPath + rm.value(), rm.method());
        return new HandlerKey(controllerLevelPath + rm.value(), rm.method());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

        HandlerKey findHandlerKey = handlerExecutions.keySet()
                .stream()
                .filter(handlerKey -> handlerKey.isSameHandlerKey(new HandlerKey(requestUri, rm)))
                .findFirst()
                .orElse(null);

        return handlerExecutions.get(findHandlerKey);
    }
}
