package core.mvc.asis;

import core.mvc.HandlerAdaptor;
import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerExecution;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecutionAdaptor implements HandlerAdaptor {

  private HandlerExecution handlerExecution;

  @Override
  public boolean isCanHandle(Object handler) {
    return handler instanceof HandlerExecution;
  }

  @Override
  public ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    handlerExecution = (HandlerExecution) handler;
    return handlerExecution.handle(req, resp);
  }
}
