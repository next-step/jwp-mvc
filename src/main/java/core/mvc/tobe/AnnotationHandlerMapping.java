package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;

public class AnnotationHandlerMapping {

  private Object[] basePackage;

  private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

  public AnnotationHandlerMapping(Object... basePackage) {
    this.basePackage = basePackage;
  }

  public void initialize() {
    try {
      handlerExecutions = getControllers(basePackage);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
  }

  private Map<HandlerKey, HandlerExecution> getControllers(Object[] basePackages)
      throws IllegalAccessException, InstantiationException {
    Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    for (Object basePackage : basePackages) {
      Reflections reflections = new Reflections(basePackage);

      Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

      for (Class clazz : typesAnnotatedWith) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
          if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            RequestMethod[] method1 = annotation.method();
            if (method1.length == 0) {
              RequestMethod[] methods = RequestMethod.values();
              for (RequestMethod requestMethod : methods) {
                String value = annotation.value();
                HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method));
              }
              continue;
            }
            for (RequestMethod requestMethod : method1) {
              String value = annotation.value();
              HandlerKey handlerKey = new HandlerKey(value, requestMethod);
              handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method));
            }
          }
        }
      }
    }
    return handlerExecutions;
  }

  public HandlerExecution getHandler(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
    return handlerExecutions.get(new HandlerKey(requestUri, rm));
  }
}
