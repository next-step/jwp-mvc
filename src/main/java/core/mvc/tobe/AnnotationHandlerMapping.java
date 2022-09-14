package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private BeanFactory beanFactory;
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Set<Class<?>> controllerTypes = controllerScanner.getControllerTypes();

        this.beanFactory = new BeanFactory(controllerTypes);
        beanFactory.initialize();

        controllerTypes.forEach(this::addHandler);
    }

    private void addHandler(Class<?> controllerType) {
        Controller controller = controllerType.getDeclaredAnnotation(Controller.class);
        String controllerPath = controller.path();

        Arrays.stream(controllerType.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    handlerExecutions.put(
                            new HandlerKey(controllerPath + requestMapping.value(), requestMapping.method()),
                            new HandlerExecution(beanFactory.getBean(controllerType), method)
                    );
                });
    }

    public boolean existHandler(HandlerKey handlerKey) {
        Objects.requireNonNull(handlerKey);
        return handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}

