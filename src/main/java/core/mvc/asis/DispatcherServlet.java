package core.mvc.asis;

import core.mvc.Mapping;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
  private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

  private Mapping[] mappings = new Mapping[2];

  @Override
  public void init() throws ServletException {
    mappings[0] = new RequestMapping();
    mappings[0].initMapping();

    mappings[1] = new AnnotationHandlerMapping("next.controller");
    mappings[1].initMapping();
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException {
    String requestUri = req.getRequestURI();
    logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

    Object controller = findController(req);

    if (controller instanceof Controller) {
      controllerExecute(req, resp, (Controller) controller);
      return;
    } else if (controller instanceof HandlerExecution) {
      annotationControllerExecute(req, resp, (HandlerExecution) controller);
      return;
    }
  }

  private void annotationControllerExecute(HttpServletRequest req, HttpServletResponse resp,
      HandlerExecution handlerExecution) {
    try {
      ModelAndView modelAndView = handlerExecution.handle(req, resp);
      modelAndView.getView().render(modelAndView.getModel(), req, resp);
    } catch (Exception e) {
      logger.error("Exception : {}", e);
    }
    return;
  }

  private void controllerExecute(HttpServletRequest req, HttpServletResponse resp,
      Controller controller) throws ServletException {
    try {
      String viewName = controller.execute(req, resp);
      move(viewName, req, resp);
    } catch (Throwable e) {
      logger.error("Exception : {}", e);
      throw new ServletException(e.getMessage());
    }
    return;
  }

  private Object findController(HttpServletRequest req) {
    for (Mapping mapping : mappings) {
      Object controller = mapping.findController(req);
      if (controller != null) {
        return controller;
      }
    }
    throw new RuntimeException("요청에 대한 Controller 가 없습니다.");
  }

  private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
      resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
      return;
    }

    RequestDispatcher rd = req.getRequestDispatcher(viewName);
    rd.forward(req, resp);
  }
}
