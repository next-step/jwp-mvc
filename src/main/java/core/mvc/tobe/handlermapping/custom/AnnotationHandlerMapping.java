package core.mvc.tobe.handlermapping.custom;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerKey;
import core.mvc.tobe.handlermapping.HandlerMapping;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        Arrays.stream(basePackage)
                .forEach(basePackage -> executeComponentScan(basePackage));
    }

    @Override
    public HandlerExecution findHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();

        if(request.getMethod() == null){
            return null;
        }

        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, method));
        return handlerExecution;
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return findHandler(request) != null;
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
