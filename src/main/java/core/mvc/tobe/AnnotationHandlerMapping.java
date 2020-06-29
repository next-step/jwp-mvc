package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.WebApplication;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.exception.HandlerMappingInitializeFailedException;
import core.utils.ComponentScanner;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@NoArgsConstructor
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Class<WebApplication> WEB_APPLICATION_ANNOTATION = WebApplication.class;
    private static final Class<Controller> HANDLER_CLASS_ANNOTATION = Controller.class;
    private static final Class<RequestMapping> HANDLER_METHOD_ANNOTATION = RequestMapping.class;

    private Object[] basePackages = {};
    private Map<HandlerKey, HandlerMethod> handlerMethods = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void initialize() {
        log.debug("handler mapping initialize");
        if (basePackages.length == 0) {
            initializeBasePackages();
        }

        try {
            ComponentScanner componentScanner = new ComponentScanner(basePackages);

            Set<Class<?>> handlerClasses = componentScanner.scanClassesBy(HANDLER_CLASS_ANNOTATION);
            for (Class<?> clazz : handlerClasses) {
                log.debug("handler class={}", clazz);

                Object instance = clazz.getDeclaredConstructor().newInstance();

                List<Method> handlerMethods = componentScanner.scanMethodsBy(clazz, HANDLER_METHOD_ANNOTATION);
                for (Method method : handlerMethods) {
                    log.debug("handler method={}", method);

                    HandlerKey handlerKey = createHandlerKeyFrom(method);
                    HandlerMethod handlerMethod = new HandlerMethod(method, instance);
                    this.handlerMethods.put(handlerKey, handlerMethod);
                }
            }
            log.debug("handler mapping initialization is over");
        } catch (ReflectiveOperationException e) {
            throw new HandlerMappingInitializeFailedException();
        }
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKeyFrom(request);

        return handlerMethods.containsKey(handlerKey);
    }

    @Override
    public HandlerMethod getHandlerMethod(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKeyFrom(request);

        return handlerMethods.get(handlerKey);
    }

    private void initializeBasePackages() {
        // TODO: 2020/06/29 어떤 basePackage 기준으로 특정 annotation이나 interface 클래스 찾아주는 util
        // TODO: 2020/06/29 ArgumentResolver 찾을 때도 사용 가능
        Reflections reflections = new Reflections("");
        this.basePackages = reflections.getTypesAnnotatedWith(WEB_APPLICATION_ANNOTATION)
                .stream()
                .map(this::findBasePackages)
                .flatMap(Arrays::stream)
                .toArray();

        if (this.basePackages.length == 0) {
            throw new IllegalStateException("Base packages not initialized");
        }
    }

    private String[] findBasePackages(Class<?> clazz) {
        String[] packages = clazz.getAnnotation(WEB_APPLICATION_ANNOTATION).basePackages();

        return packages.length == 0 ? new String[]{clazz.getPackage().getName()} : packages;
    }

    private HandlerKey createHandlerKeyFrom(Method handlerMethod) {
        RequestMapping annotation = handlerMethod.getAnnotation(HANDLER_METHOD_ANNOTATION);
        return new HandlerKey(annotation.value(), annotation.method());
    }

    private HandlerKey createHandlerKeyFrom(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return new HandlerKey(requestUri, requestMethod);
    }
}
