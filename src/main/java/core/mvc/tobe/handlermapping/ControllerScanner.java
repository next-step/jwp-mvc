package core.mvc.tobe.handlermapping;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import core.annotation.web.Controller;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handler.HandlerKey;
import core.mvc.tobe.handlermapping.exception.ControllerScanException;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class ControllerScanner {
    public static HandlerExecutions scan(Object basePackage) {
        Map<HandlerKey, HandlerExecution> collect = getTypesAnnotatedWith(Controller.class, basePackage)
                .stream()
                .map(clazz -> HandlerExecutions.init(clazz))
                .map(HandlerExecutions::getHandlerExecutions)
                .collect(collectingAndThen(toList(), Collection::stream))
                .flatMap(m -> m.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new HandlerExecutions(collect);
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
