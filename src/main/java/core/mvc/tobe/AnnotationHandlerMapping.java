package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.BeanFactory;
import core.mvc.HandlerMapping;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private BeanFactory beanFactory;
    private Object[] basePackage;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Set<Class<?>> controllerTypes = controllerScanner.getControllers().keySet();
        this.beanFactory = new BeanFactory(controllerTypes);
        beanFactory.initialize();
        controllerTypes.forEach(controllerType -> getRequestMappingMethods(controllerType));
    }

    private HandlerKey createHandlerKey(RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    private void getRequestMappingMethods(Class<?> controllerType) {
        final Set<Method> methods = ReflectionUtils.getAllMethods(controllerType, ReflectionUtils.withAnnotation(RequestMapping.class));
        methods.forEach(
                method -> {
                    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    final HandlerKey handlerKey = createHandlerKey(requestMapping);
                    handlerExecutions.put(handlerKey, new HandlerExecution(method, beanFactory.getBean(controllerType)));
                }
        );
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
