package core.mvc.asis;

import core.mvc.HandlerAdapter;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import core.mvc.tobe.RequestControllerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private Set<HandlerAdapter> handlerAdapters = Set.of(
            new ControllerHandlerAdapter(),
            new HandlerExecutionHandlerAdapter()
    );

    private Set<HandlerMapping> handlerMappings;

    @Override
    public void init() throws ServletException {
        RequestMapping requestMapping = new RequestMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();

        requestMapping.initMapping();
        annotationHandlerMapping.initialize();

        handlerMappings = Set.of(
                new RequestControllerMapping(requestMapping),
                annotationHandlerMapping
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = handlerMappings.stream()
                .map(it -> it.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());

        HandlerAdapter handlerAdapter = handlerAdapters.stream().filter(it -> it.support(handler))
                .findFirst()
                .orElseThrow();

        try {
            ModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);
            render(modelAndView, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
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
