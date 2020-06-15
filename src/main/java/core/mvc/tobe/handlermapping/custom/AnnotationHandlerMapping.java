package core.mvc.tobe.handlermapping.custom;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Arrays.stream(basePackage)
                .forEach(basePackage -> executeComponentScan(basePackage));
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());

        return handlerExecutions.get(new HandlerKey(requestUri, method));
    }

    private Object getNewInstance(Class clazz) {
        Object controller = null;

        try {
            controller = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return controller;
    }

    private void executeComponentScan(Object basePackage) {
        Reflections reflections = new Reflections(basePackage,
                new TypeAnnotationsScanner(),
                new SubTypesScanner());

        reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .forEach(this::initHandlerExecutions);
    }

    private void initHandlerExecutions(Class clazz) {
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> method.getDeclaredAnnotation(RequestMapping.class))
                .map(annotation -> new HandlerKey(annotation.value(), annotation.method()))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(getNewInstance(clazz))));
    }
}
