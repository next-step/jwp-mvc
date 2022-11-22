package core.mvc.asis;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.HandlerNotFoundException;
import core.mvc.view.ModelAndView;
import core.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final List<HandlerMapping> mappings = new ArrayList<>();

    @Override
    public void init() {
        RequestMappingLegacy requestMappingLegacy = new RequestMappingLegacy();
        requestMappingLegacy.initMapping();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();

        mappings.add(requestMappingLegacy);
        mappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            Object handler = getHandler(req);

            if (handler instanceof ControllerLegacy) {
                String viewName = ((ControllerLegacy) handler).execute(req, resp);
                move(viewName, req, resp);
            } else if (handler instanceof HandlerExecution) {
                ModelAndView modelAndView = ((HandlerExecution) handler).handle(req, resp);

                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), req, resp);
            } else {
                throw new HandlerNotFoundException(req.getRequestURI());
            }
        } catch (Throwable e) {
            logger.error("Exception: {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return mappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(request)))
                .findFirst()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .orElseThrow(() -> new HandlerNotFoundException(request.getRequestURI()));
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
