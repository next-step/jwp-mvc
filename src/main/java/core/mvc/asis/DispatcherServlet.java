package core.mvc.asis;

import core.mvc.HandlerAdaptor;
import core.mvc.Mapping;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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


  private List<Mapping> mappings = new ArrayList<>();
  private List<HandlerAdaptor> handlerAdaptors;

  @Override
  public void init() {
    mappings.add(new RequestMapping());
    mappings.add(new AnnotationHandlerMapping("next.controller"));

    mappings.stream()
        .forEach(mapping -> mapping.initMapping());

    handlerAdaptors = Arrays.asList(new ControllerAdaptor(), new HandlerExecutionAdaptor());
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

    Object handler = findController(req);
    HandlerAdaptor adaptor = findAdaptor(handler);

    try {
      ModelAndView modelAndView = adaptor.handle(handler, req, resp);
      modelAndView.render(modelAndView.getModel(), req, resp);
    } catch (Exception e) {
      logger.error("error : {}", e.getMessage());
    }
    
  }

  private HandlerAdaptor findAdaptor(Object handler) {
    return handlerAdaptors.stream()
        .filter(adaptor -> adaptor.isCanHandle(handler))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Controller를 수행할 Adaptor가 없습니다."));
  }

  private Object findController(HttpServletRequest req) {
    return mappings.stream()
        .map(mapping -> mapping.findController(req))
        .filter(controller -> controller != null)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("요청에 대한 Controller 가 없습니다."));
  }
}
