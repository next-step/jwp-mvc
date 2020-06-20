package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HandlerMapping;
import core.mvc.exceptions.HandlerNotFoundException;
import core.mvc.scanner.AnnotationScanner;
import core.mvc.utils.ReflectionUtil;
import core.mvc.utils.UnableToCreateInstanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
        try {
            final Object instance = ReflectionUtil.instantiateClass(clazz);
            AnnotationScanner.loadMethods(clazz, RequestMapping.class)
                    .forEach(method -> storeHandlerMap(method, instance));
        } catch (UnableToCreateInstanceException e) {
            log.error(e.getMessage(), e);
        }
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
        final StringBuilder sb = new StringBuilder();
        if (!root.startsWith("/") && !relativePath.startsWith("/")) {
            sb.append("/");
        }
        sb.append(root);
        if (!relativePath.startsWith("/")) {
            sb.append("/");
        }
        sb.append(relativePath);
        return sb.toString();
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) throws HandlerNotFoundException {
        final String requestUri = request.getRequestURI();
        final RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        final HandlerKey key = new HandlerKey(requestUri, rm);
        return handlerExecutions.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException("핸들러가 존재하지 않아요!"))
                .getValue();
    }

}
