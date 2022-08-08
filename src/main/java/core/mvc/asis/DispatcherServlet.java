package core.mvc.asis;

import core.mvc.ForwardView;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.RedirectView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.ControllerScanner;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> mappings;

    @Override
    public void init() {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.initMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(new ControllerScanner("next.controller"));
        annotationHandlerMapping.initialize();
        mappings = List.of(requestMapping, annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
        renderView(handler(req), req, resp);
    }

    private Object handler(HttpServletRequest request) throws ServletException {
        return mappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ServletException(String.format("can not found request mapping: request(%s)", request)));
    }

    private void renderView(Object handler, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            modelAndView(handler, req, resp).viewRender(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView modelAndView(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof Controller) {
            String executedResult = ((Controller) handler).execute(req, resp);
            return ModelAndView.from(view(executedResult));
        }
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(req, resp);
        }
        throw new UnsupportedOperationException(String.format("handler(type: %s) is not supported type", handler.getClass()));
    }

    private View view(String viewName) {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return RedirectView.from(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }
        return ForwardView.from(viewName);
    }
}
