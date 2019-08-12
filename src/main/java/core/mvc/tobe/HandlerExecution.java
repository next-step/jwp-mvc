package core.mvc.tobe;

import core.mvc.ModelAndView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecution {

  private Class handler;
  private Method method;

  public HandlerExecution(Class handler, Method method) {
    this.handler = handler;
    this.method = method;
  }

  public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
      throws IllegalAccessException, InstantiationException, InvocationTargetException {
    return (ModelAndView) method.invoke(handler.newInstance(), request, response);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HandlerExecution that = (HandlerExecution) o;
    return Objects.equals(handler, that.handler) &&
        Objects.equals(method, that.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(handler, method);
  }
}
