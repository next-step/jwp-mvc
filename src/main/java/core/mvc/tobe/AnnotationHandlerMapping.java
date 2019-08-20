package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.Mapping;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements Mapping {

  private Object[] basePackage;

  private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

  public AnnotationHandlerMapping(Object... basePackage) {
    this.basePackage = basePackage;
  }

  public void initMapping() {
    initHandlerExecutions(basePackage);
  }

  private void initHandlerExecutions(Object[] basePackages) {
    ControllerScanner controllerScanner = new ControllerScanner(basePackages);
    Map<Class, Object> controllers = controllerScanner.getControllers();
    controllers.keySet().stream()
        .forEach(controller ->
            makeHandlerExecutionsAndFill(controllers.get(controller),
                getHandlerExecutionMethods(controller))
        );
  }

  private List<Method> getHandlerExecutionMethods(Class handlers) {
    return Arrays.stream(handlers.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
        .collect(Collectors.toList());
  }

  private void makeHandlerExecutionsAndFill(Object handler, List<Method> executionMethods) {
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

  public HandlerExecution findController(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
    return handlerExecutions.get(new HandlerKey(requestUri, rm));
  }

}
