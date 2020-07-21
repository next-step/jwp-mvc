package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.AnnotationScanner;
import core.mvc.HandlerMapping;
import core.mvc.tobe.helper.*;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    private List<HandlerMethodHelper> methodHelpers = new ArrayList<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        this.initializeMethodHelper();
        AnnotationScanner annotationScanner = new AnnotationScanner(this.basePackage);
        Set<Class<?>> controllers = annotationScanner.findAnnotationClass(Controller.class);

        for (Class<?> controller : controllers) {
            this.handlerExecutions.putAll(Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toMap(method -> {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        return new HandlerKey(requestMapping.value(), requestMapping.method());
                    }, method -> new HandlerExecution(controller, method, methodHelpers))));
        }

    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.getOrDefault(new HandlerKey(requestUri, rm), null);
    }

    private void initializeMethodHelper() {
        methodHelpers.add(new AnnotationParameterHelper());
        methodHelpers.add(new ObjectParameterHelper());
        methodHelpers.add(new OriginalParameterHelper());
    }
}
