package core.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        // 컴포넌트 스캔
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(controller -> {
            // 메소드 찾기
            Method[] declaredMethods = controller.getDeclaredMethods();
            List<Method> methods = Arrays.stream(declaredMethods)
                                         .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                         .collect(Collectors.toList());

            try {
                // 객체 생성 및 등록
                Object instance = controller.getDeclaredConstructor().newInstance();
                methods.forEach(method -> handlerExecutions.put(getHandlerKey(method), new HandlerExecution(instance, method)));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    public static HandlerKey getHandlerKey(Method method) {
        String path = method.getAnnotation(RequestMapping.class).value();
        RequestMethod requestMethod = method.getAnnotation(RequestMapping.class).method();

        return new HandlerKey(path, requestMethod);
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
