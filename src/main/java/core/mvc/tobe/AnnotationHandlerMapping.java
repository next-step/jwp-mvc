package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.exception.ExceptionWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class AnnotationHandlerMapping {
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws ClassNotFoundException {
        if (ArrayUtils.isEmpty(basePackage)) {
            throw new ClassNotFoundException("base package를 설정해주세요.");
        }

        final Reflections reflections = new Reflections(configurationBuilder(basePackage));
        final ControllerAnnotationScanner controllerAnnotationScanner = new ControllerAnnotationScanner(reflections);

        final Stream<Method> methods = controllerAnnotationScanner.filteredMethodsBy(RequestMapping.class);
        put(methods);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private ConfigurationBuilder configurationBuilder(final Object... basePackage) {
        return ConfigurationBuilder
                .build(basePackage)
                .setScanners(
                        new SubTypesScanner(),
                        new FieldAnnotationsScanner(),
                        new MethodAnnotationsScanner(),
                        new TypeAnnotationsScanner())
                .setExecutorService(Executors.newFixedThreadPool(4));
    }

    private void put(Stream<Method> methods) {
        methods.forEach(ExceptionWrapper.consumer(method -> {
            final HandlerKey handlerKey = createHandlerKeyByRequestMapping(method);
            final HandlerExecution handlerExecution = createHandlerExecution(method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }));
    }

    private HandlerKey createHandlerKeyByRequestMapping(final Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final RequestMethod requestMethod = annotation.method();
        final String url = annotation.value();

        return new HandlerKey(url, requestMethod);
    }

    private HandlerExecution createHandlerExecution(Method method) throws InstantiationException, IllegalAccessException {
        return new HandlerExecution(method.getDeclaringClass().newInstance(), method);
    }
}
