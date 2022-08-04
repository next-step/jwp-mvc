package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.BeanFactory;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class AnnotationHandlerMapping {
    private BeanFactory beanFactory;
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(this.basePackage);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        this.beanFactory = new BeanFactory(controllerTypes);
        beanFactory.initialize();

        controllerTypes.forEach(this::addHandlers);
    }

    private void addHandlers(Class<?> controllerType) {
        Controller controllerAnno = controllerType.getDeclaredAnnotation(Controller.class);
        String controllerPath = controllerAnno.path();

        Arrays.stream(controllerType.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(addHandlerAction(controllerType, controllerPath));
    }

    private Consumer<Method> addHandlerAction(Class<?> controllerType, String controllerPath) {
        return method -> {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);

            HandlerKey handlerKey = new HandlerKey(controllerPath + rm.value(), rm.method());
            handlerExecutions.put(handlerKey, new HandlerExecution(beanFactory.getBean(controllerType), method));
        };
    }


    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestUri, rm));

        if (Objects.isNull(handlerExecution)) {
            return handlerExecutions.get(new HandlerKey(requestUri, RequestMethod.NONE));
        }
        return handlerExecution;
    }
}
