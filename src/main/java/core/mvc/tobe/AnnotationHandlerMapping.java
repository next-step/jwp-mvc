package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.exceptions.HandlerNotFoundException;
import core.mvc.scanner.AnnotationScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        final Set<Class<?>> targetControllers = AnnotationScanner.loadClasses(Controller.class, basePackage);
        targetControllers.forEach(this::convertClassToHandler);
    }

    private void convertClassToHandler(Class<?> clazz) {
        final Object instance = instantiateClass(clazz);
        if (instance == null) {
            return;
        }

        AnnotationScanner.loadMethods(clazz, RequestMapping.class)
                .forEach(method -> storeHandlerMap(method, instance));
    }

    private void storeHandlerMap(Method method, Object instance) {
        final String rootPath = getRootPathFromInstance(instance);
        final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
        final RequestMapping mappingInfo = method.getAnnotation(RequestMapping.class);
        Arrays.stream(mappingInfo.method())
                .forEach(requestMethod -> {
                    this.handlerExecutions.put(
                            new HandlerKey(resolveUri(rootPath, mappingInfo.value()), requestMethod),
                            handlerExecution
                    );
                });
    }

    private String getRootPathFromInstance(Object instance) {
        // request mapping의 retention policy가 type과 method이므로 스프링의 request mapping처럼 동작하려면 필요.
        return Optional
                .ofNullable(instance.getClass().getAnnotation(RequestMapping.class))
                .map(RequestMapping::value)
                .orElse("");
    }

    private String resolveUri(String root, String relativePath) {
        try {
            final URI uri = new URI(root);
            return uri.resolve(relativePath).toString();
        } catch (URISyntaxException e) {
            return relativePath;
        }
    }

    // TODO: 흠.. Reflection과 관련된 util로 빼도 되지 않을까?? - 필요해지면 그때 그렇게 합시다!
    private Object instantiateClass(Class<?> clazz) {
        return createInstance(determineNonArgsConstructor(clazz));
    }

    private Constructor<?> determineNonArgsConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    private Object createInstance(Constructor<?> constructor) {
        try {
            if (constructor == null) {
                return null;
            }
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) throws HandlerNotFoundException {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return Optional
                .ofNullable(handlerExecutions.get(new HandlerKey(requestUri, rm)))
                .orElseThrow(() -> new HandlerNotFoundException("핸들러가 존재하지 않아요!"));
    }
}
