package core.mvc.tobe.handlermapping.custom;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.mvc.tobe.handler.HandlerExecution;
import core.mvc.tobe.handler.HandlerExecutions;
import core.mvc.tobe.handler.HandlerKey;
import core.mvc.tobe.handlermapping.HandlerMapping;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Reflections REFLECTIONS;

    private Object basePackage;
    private HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.REFLECTIONS = new Reflections(basePackage, new TypeAnnotationsScanner(), new SubTypesScanner());
    }

    @Override
    public void init() {
        this.handlerExecutions = executeComponentScan();
    }

    @Override
    public boolean hasHandler(HttpServletRequest request) {
        return findHandler(request) != null;
    }

    @Override
    public HandlerExecution findHandler(HttpServletRequest request) {
        return handlerExecutions.getValueByKey(HandlerKey.of(request));
    }

    private HandlerExecutions executeComponentScan() {
        Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

        REFLECTIONS.getTypesAnnotatedWith(Controller.class)
                .stream()
                .map(clazz -> HandlerExecutions.init(clazz))
                .forEach(he -> handlerExecutions.putAll(he.getHandlerExecutions()));

        return new HandlerExecutions(handlerExecutions);
    }
}
