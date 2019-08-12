package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exceptions.AnnotationHandlerMappingException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        try {
            basePackageInitialize(basePackage);
        } catch (AnnotationHandlerMappingException e) {
            logger.error("{}", e);
            throw e;
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void basePackageInitialize(Object[] bases) {
        for (Object base : bases) {
            Reflections reflections = getBasePackageReflections(base);
            Set<Class<?>> controllerClasses = getControllerClasses(reflections);
            Set<HandlerExecution> handlerExecutions = getHandlerExecurions(controllerClasses);
            Map<HandlerKey, HandlerExecution> keyHandlerExecutions = getKeyHandlerExecutions(handlerExecutions);
            this.handlerExecutions.putAll(keyHandlerExecutions);
        }
    }

    private Map<HandlerKey, HandlerExecution> getKeyHandlerExecutions(Set<HandlerExecution> handlerExecutions) {
        return handlerExecutions.stream()
                .collect(toMap(this::getHandlerKey, Function.identity(), (v1, v2) -> v1));
    }

    private HandlerKey getHandlerKey(HandlerExecution handlerExecution) {
        Method method = handlerExecution.getInvokeMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (requestMapping == null) {
            throw new AnnotationHandlerMappingException("getHandlerKey fail by : " + RequestMapping.class);
        }

        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    private Set<HandlerExecution> getHandlerExecurions(Set<Class<?>> controllerClasses) {

        return Optional.ofNullable(controllerClasses)
                .orElse(emptySet())
                .stream()
                .flatMap(ctrl -> getHandlerExecurions(ctrl).stream())
                .collect(toSet());
    }

    private Set<HandlerExecution> getHandlerExecurions(Class<?> controllerClasses) {
        Object invokeInstance = getControllerNew(controllerClasses);

        return getRequestMappingMethod(controllerClasses)
                .stream()
                .map(invokeMethod -> HandlerExecution.of(invokeInstance, invokeMethod))
                .collect(toSet());
    }

    private Object getControllerNew(Class<?> controllerClasses) {
        try {
            return controllerClasses.newInstance();
        } catch (IllegalAccessException e) {
            throw new AnnotationHandlerMappingException(e);
        } catch (InstantiationException e) {
            throw new AnnotationHandlerMappingException(e);
        }
    }

    private Reflections getBasePackageReflections(Object basePackage) {
        return new Reflections(
                ConfigurationBuilder.build(basePackage)
                        .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
        );
    }

    private Set<Class<?>> getClassesByAnnotation(Reflections reflections, Class<? extends Annotation> annotationClass) {
        return reflections.getTypesAnnotatedWith(annotationClass);
    }

    private Set<Class<?>> getControllerClasses(Reflections reflections) {
        return getClassesByAnnotation(reflections, Controller.class);
    }

    private Set<Method> getMethodByAnnotation(Class<?> controllerClass, Class<? extends Annotation> annotationClass) {
        return new Reflections(
                ConfigurationBuilder.build(controllerClass)
                        .setScanners(new MethodAnnotationsScanner())
        ).getMethodsAnnotatedWith(annotationClass);
    }

    private Set<Method> getRequestMappingMethod(Class<?> controllerClass) {
        return getMethodByAnnotation(controllerClass, RequestMapping.class);
    }

    private Set<Method> getFilteredMethods(Set<Method> methods, Class<?> returnType, Class<?>[] argumentTypes) {
        return Optional.ofNullable(methods)
                .orElse(emptySet())
                .stream()
                .filter(method -> method.getReturnType() == returnType)
                .filter(method -> Arrays.equals(method.getParameterTypes(), argumentTypes))
                .collect(toSet());
    }
}
