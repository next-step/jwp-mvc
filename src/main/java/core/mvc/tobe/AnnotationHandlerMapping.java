package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(
            basePackage,
            new MethodAnnotationsScanner(),
            new TypeAnnotationsScanner(),
            new SubTypesScanner()
        );

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class clazz : controllers) {
            Object bean = newInstance(clazz);
            List<Method> methods = findRequestMappingMethod(clazz);
            methods.stream().forEach(method -> addRequestMapping(bean, method));
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }

    private void addRequestMapping(Object bean, Method method) {
        Controller controller = method.getDeclaringClass().getAnnotation(Controller.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        String url = controller.value() + requestMapping.value();
        HandlerKey handlerKey = new HandlerKey(url, requestMapping.method());
        handlerExecutions.put(handlerKey, (request, response) -> (ModelAndView) method.invoke(bean, request, response));
    }

    private Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new AnnotationHandlingException();
        }
    }

    private List<Method> findRequestMappingMethod(Class clazz) {
        return Arrays.asList(clazz.getDeclaredMethods())
            .stream()
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }
}
