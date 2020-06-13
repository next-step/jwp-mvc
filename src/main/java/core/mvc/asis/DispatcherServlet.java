package core.mvc.asis;

import core.exception.NotFoundException;
import core.mvc.*;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String BASE_PACKAGE = "next.controller";

    private List<RequestHandlerMapping> requestHandlerMappings;
    private RequestMapping requestMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() throws ServletException {
        requestHandlerMappings = new ArrayList<>();

        annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();

        requestMapping = new RequestMapping();
        requestMapping.initMapping();

        requestHandlerMappings.add(annotationHandlerMapping);
        requestHandlerMappings.add(requestMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            Object handler = findHandler(req);

            handle(handler, req, resp);
        } catch (NotFoundException e) {
            logger.error("{}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = DummyView.INSTANCE;
        Map<String, Object> model = Collections.emptyMap();

        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(req, resp);

            view = resolve(viewName, req, resp);
        } else if (handler instanceof HandlerExecution){
            ModelAndView modelAndView = ((HandlerExecution) handler).handle(req, resp);

            view = extractView(req, resp, modelAndView);
            model = modelAndView.getModel();
        }

        view.render(model, req, resp);
    }

    private View extractView(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws IOException {
        if (modelAndView.getView() != null) {
            return modelAndView.getView();
        } else {
            String viewName = modelAndView.getViewName();
            return resolve(viewName, req, resp);
        }
    }

    private View resolve(String viewName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return DummyView.INSTANCE;
        }

        return new JspView(viewName);
    }

    private Object findHandler(HttpServletRequest req) {
        return requestHandlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(req.getRequestURI()));
    }
}
