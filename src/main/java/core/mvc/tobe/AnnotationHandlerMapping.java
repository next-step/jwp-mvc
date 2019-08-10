package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.Mapping;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements Mapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            try {
                addHandlerExecutions(controller);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException("컨트롤러 초기화 중 오류가 발생했습니다.", e);
            }
        }
    }

    private void addHandlerExecutions(Class<?> controllerClass) throws IllegalAccessException, InstantiationException {
        Object controller = controllerClass.newInstance();
        for (Method method : controllerClass.getMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            handlerExecutions.put(createHandlerKey(method), new HandlerExecution(controller, method));
        }
    }

    private HandlerKey createHandlerKey(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
