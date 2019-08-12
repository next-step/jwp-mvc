package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.RequestHandler;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandler;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next";

    private List<RequestHandler> requestHandlers;

    @Override
    public void init() throws ServletException {
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.initMapping();

        AnnotationHandler annotationHandlerMapping = new AnnotationHandler(BASE_PACKAGE);
        annotationHandlerMapping.initialize();

        requestHandlers = new ArrayList<>();
        requestHandlers.add(requestMapping);
        requestHandlers.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            ModelAndView modelAndView = handle(req, resp);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
        }
    }

    private ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object handler = getRequestHandler(req);

        if (handler instanceof Controller) {
            return ((Controller) handler).execute(req, resp);
        } else {
            return ((HandlerExecution) handler).handle(req, resp);
        }
    }

    private Object getRequestHandler(HttpServletRequest req) {
        return requestHandlers.stream()
                .map(requestHandler -> requestHandler.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not existing URL"));
    }
}
