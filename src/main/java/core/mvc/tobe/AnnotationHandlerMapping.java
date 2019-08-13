package core.mvc.tobe;

import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exceptions.HandlerMappingException;
import core.mvc.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping<HandlerExecution> {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        try {
            ControllerAnnotationScanner scanner = ControllerAnnotationScanner.getScanner(this.basePackage);
            Map<Class<?>, Object> instantiateControllers = scanner.getInstantiateControllers();
            Set<Method> requestMappingMethods = getAllRequestMappingMethod(instantiateControllers.keySet());
            setHandlerExecutions(instantiateControllers, requestMappingMethods);
        } catch (HandlerMappingException e) {
            logger.error("{}", e);
            throw e;
        }
    }
    
    @Override
	public boolean support(HttpServletRequest request) {
		return handlerExecutions.containsKey(createHandlerKey(request));
	}

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        return handlerExecutions.get(createHandlerKey(request));
    }
    
    private HandlerKey createHandlerKey(HttpServletRequest request) {
    	String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return new HandlerKey(requestUri, rm);
    }

    private void setHandlerExecutions(Map<Class<?>, Object> instantiateControllers, Set<Method> requestMappingMethods) {
        for (Method method : requestMappingMethods) {
            Class<?> declaringClass = method.getDeclaringClass();
            Set<HandlerKey> handlerKeys = getHandlerKeys(method);
            HandlerExecution handlerExecution = HandlerExecution.of(instantiateControllers.get(declaringClass), method);
            puttHandlerExecutionWithKeys(handlerKeys, handlerExecution);
        }
    }

    private void puttHandlerExecutionWithKeys(Set<HandlerKey> handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            this.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Set<HandlerKey> getHandlerKeys(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (requestMapping == null) {
            throw new HandlerMappingException("getHandlerKey fail by : " + RequestMapping.class);
        }

        return Optional.ofNullable(requestMapping.method())
                .map(Arrays::asList)
                .filter(list -> !CollectionUtils.isEmpty(list))
                .map(Collection::stream)
                .orElse(Arrays.stream(RequestMethod.values()))
                .map(requestMappingMethod -> new HandlerKey(requestMapping.value(), requestMappingMethod))
                .collect(toSet());
    }

    private Set<Method> getAllRequestMappingMethod(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .map(this::getRequestMappingMethod)
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    private Set<Method> getRequestMappingMethod(Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
    }
}
