package core.mvc.tobe.handlermapping;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handler.HandlerKey;
import core.mvc.tobe.handlermapping.exception.ControllerScanException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Map;
import java.util.Set;

public class ControllerScanner {
    public static HandlerExecutions scan(Object basePackage) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

        getTypesAnnotatedWith(Controller.class, basePackage)
                .stream()
                .map(clazz -> HandlerExecutions.init(clazz))
                .forEach(handler -> handlerExecutions.putAll(handler.getHandlerExecutions()));

        return new HandlerExecutions(handlerExecutions);
    }

    private static Set<Class> getTypesAnnotatedWith(Class clazz, Object basePackage) {
        try {
            Reflections reflections = new Reflections(basePackage, new TypeAnnotationsScanner(), new SubTypesScanner());
            return reflections.getTypesAnnotatedWith(clazz);
        } catch (Exception e) {
            throw new ControllerScanException(e);
        }

    }
}
