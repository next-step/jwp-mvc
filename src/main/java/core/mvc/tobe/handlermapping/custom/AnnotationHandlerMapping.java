package core.mvc.tobe.handlermapping.custom;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerKey;
import core.mvc.tobe.handlermapping.HandlerMapping;
import core.mvc.tobe.handlermapping.exception.InstanceNotCreatedException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.LambdaConversionException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Reflections REFLECTIONS;

    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.REFLECTIONS = new Reflections(basePackage, new TypeAnnotationsScanner(), new SubTypesScanner());
    }

    @Override
    public void init() {
        Arrays.stream(basePackage)
                .forEach(basePackage -> executeComponentScan());
    }

    @Override
    public HandlerExecution findHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return findHandler(request) != null;
    }

    private void executeComponentScan() {
        REFLECTIONS.getTypesAnnotatedWith(Controller.class)
                .stream()
                .filter(clazz -> isNonNull(createInstance(clazz)))
                .forEach(this::initHandlerExecutions);
    }

    private void initHandlerExecutions(Class clazz) {
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> method.getDeclaredAnnotation(RequestMapping.class))
                .map(annotation -> new HandlerKey(annotation.value(), annotation.method()))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, createHandlerExecution(clazz)));
    }

    private HandlerExecution createHandlerExecution(Class clazz) {
        try {
            return new HandlerExecution(clazz.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new InstanceNotCreatedException(e);
        }
    }

    private boolean isNonNull(Object object){
        return object != null;
    }

    private Object createInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new InstanceNotCreatedException(e);
        }
    }
}
