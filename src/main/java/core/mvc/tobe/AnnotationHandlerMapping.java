package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.reflections.Reflections;

public class AnnotationHandlerMapping {

  private Object[] basePackage;

  private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

  public AnnotationHandlerMapping(Object... basePackage) {
    this.basePackage = basePackage;
  }

  public void initialize() {
    initHandlerExecutions(basePackage);
  }

  private void initHandlerExecutions(Object[] basePackages) {
    for (Object basePackage : basePackages) {
      initHandlerExecutions(basePackage);
    }
  }

  private void initHandlerExecutions(Object basePackage) {
    Set<Class<?>> handlers = getHandlers(basePackage);

    for (Class handler : handlers) {
      makeHandlerExecutionsAndFill(handler, getHandlerExecutionMethods(handler));
    }
  }

  private Set<Class<?>> getHandlers(Object basePackage) {
    return getAnnotationTypeClass(basePackage, Controller.class);
  }

  private List<Method> getHandlerExecutionMethods(Class handlers) {
    return Arrays.stream(handlers.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
        .collect(Collectors.toList());
  }

  private void makeHandlerExecutionsAndFill(Class handler, List<Method> executionMethods) {
    for (Method method : executionMethods) {
      RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

      RequestMethod[] requestMethods = getRequestMethods(requestMapping.method());
      HandlerExecution handlerExecution = new HandlerExecution(handler, method);
      HandlerKey[] handlerKey = makeHandlerKey(requestMethods, requestMapping.value());
      
      fillHandlerExecutions(handlerExecution, handlerKey);
    }
  }

  private void fillHandlerExecutions(HandlerExecution handlerExecution, HandlerKey[] handlerKey) {
    for (HandlerKey key : handlerKey) {
      handlerExecutions.put(key, handlerExecution);
    }
  }

  private HandlerKey[] makeHandlerKey(RequestMethod[] requestMethods, String url) {
    HandlerKey[] handlerKeys = new HandlerKey[requestMethods.length];
    for (int i = 0; i < requestMethods.length; i++) {
      handlerKeys[i] = new HandlerKey(url, requestMethods[i]);
    }
    return handlerKeys;
  }

  private RequestMethod[] getRequestMethods(RequestMethod[] methods) {
    if (methods.length > 0) {
      return methods;
    }
    return RequestMethod.values();
  }

  private Set<Class<?>> getAnnotationTypeClass(Object basePackage, Class clazz) {
    Reflections reflections = new Reflections(basePackage);
    return reflections.getTypesAnnotatedWith(clazz);
  }

  public HandlerExecution getHandler(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
    return handlerExecutions.get(new HandlerKey(requestUri, rm));
  }
}
