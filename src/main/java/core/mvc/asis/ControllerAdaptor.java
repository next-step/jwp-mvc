package core.mvc.asis;

import core.mvc.HandlerAdaptor;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerAdaptor implements HandlerAdaptor {

  private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
  Controller controller;

  @Override
  public boolean isCanHandle(Object handler) {
    return handler instanceof Controller;
  }

  @Override
  public ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    controller = (Controller) handler;
    String viewName = controller.execute(req, resp);
    return move(viewName);
  }

  private ModelAndView move(String viewName) {
    if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
      return new ModelAndView(
          new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length())));
    }
    return new ModelAndView(new JspView(viewName));
  }

}
