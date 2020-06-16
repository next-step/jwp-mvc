package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 1. base 패키지에 있는 class 들 scan
     * 2. class 정보 별로 handlerkey / handlerExecution을 만들어 handlerExecutions 에 넣음
     *
     *  HadlerKey = url(String) , requestMethod (GET,POST,PUT,DELETE....)
     *  HandleExecutions handle구현 (method invoke?)
     */
    public void initialize() {
        Reflections reflections = new Reflections(basePackage, new TypeAnnotationsScanner());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class,true);

        for(Class<?> controller : controllers) {
            this.handlerExecutions = Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toMap(method -> {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        return new HandlerKey(requestMapping.value(), requestMapping.method());
                        }, method -> new HandlerExecution(controller,method)));
        }

    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
