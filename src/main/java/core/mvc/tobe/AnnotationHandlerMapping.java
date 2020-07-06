package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.Handler;
import core.mvc.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Set<Class<?>> controllers = ControllerScanner.scan(Controller.class, basePackage);
        handlerExecutions = controllers.stream()
                .map(this::initializeMethodsOf)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        handlerExecutions.keySet().forEach(handlerKey -> log.info(handlerKey.toString()));
    }

    private Map<HandlerKey, HandlerExecution> initializeMethodsOf(Class<?> controller) {
        Object instance = createInstance(controller);
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(toMap(
                        HandlerKey::from,
                        method -> new HandlerExecution(method, instance)));
    }

    private Object createInstance(Class<?> controller) {
        try {
            return controller.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.debug("fail to initialize method of {}", controller.getSimpleName());
            throw new IllegalArgumentException();
        }
    }


    @Override
    public Handler getHandler(HttpServletRequest request) {
        String requestUri = getRequestUri(request);
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private String getRequestUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return handlerExecutions.keySet().stream()
                .map(HandlerKey::getUrl)
                .filter(mappingUri -> match(mappingUri, requestUri))
                .findFirst()
                .orElse(requestUri);
    }

    private boolean match(String mappingUri, String requestUri) {
        PathPattern pathPattern = parse(mappingUri);
        return pathPattern.matches(toPathContainer(requestUri));
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
