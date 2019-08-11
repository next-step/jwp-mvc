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
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

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
        RequestMethod rm = RequestMethod.valueOfMethod(request.getMethod());
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
            final Set<HandlerKey> handlerKeys = createHandlerKeyByRequestMapping(method);
            final HandlerExecution handlerExecution = createHandlerExecution(method);

            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }));
    }

    private Set<HandlerKey> createHandlerKeyByRequestMapping(final Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final Stream<RequestMethod> requestMethods = getRequestMethods(annotation);
        final String url = annotation.value();

        return requestMethods.map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(toSet());
    }

    private Stream<RequestMethod> getRequestMethods(RequestMapping annotation) {
        Stream<RequestMethod> requestMethods = Arrays.stream(annotation.method());
        if (requestMethods.count() == 0) {
            return Arrays.stream(RequestMethod.values());
        }

        return Arrays.stream(annotation.method());
    }

    private HandlerExecution createHandlerExecution(Method method) throws InstantiationException, IllegalAccessException {
        return new HandlerExecution(method.getDeclaringClass().newInstance(), method);
    }
}
