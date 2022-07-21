package core.mvc;

import core.mvc.exception.NotFoundException;
import core.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            handle(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = getHandler(request);
        if (handler instanceof HandlerExecution) {
            render(
                    ((HandlerExecution) handler).handle(request, response),
                    request,
                    response
            );
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(it -> it.hasHandler(request))
                .findAny()
                .orElseThrow(() -> new NotFoundException(request))
                .getHandler(request);
    }

    private void render(
            ModelAndView modelAndView,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        modelAndView.getView()
                .render(modelAndView.getModel(), request, response);
    }
}
