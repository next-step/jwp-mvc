package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.ModelAndViewGettable;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet implements ModelAndViewGettable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_CONTROLLER_PACKAGE = "next.controller.tobe";

    private RequestMapping requestMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping(BASE_CONTROLLER_PACKAGE);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Object handler = getHandler(req);
            ModelAndView mav = handle(req, resp, handler);
            render(mav, req, resp);
        }
        catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest req) {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = requestMapping.findController(requestUri);

        if (Objects.nonNull(controller)) {
            return controller;
        }

        return annotationHandlerMapping.getHandler(req);
    }

    private ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        if (handler instanceof Controller) {
            return getModelAndView(req, ((Controller)handler).execute(req, resp));
        }

        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution)handler).handle(req, resp);
        }

        return new ModelAndView(new JspView(req.getRequestURI()));
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();

        if (Objects.isNull(view)) {
            view = new JspView(req.getRequestURI());
        }

        view.render(mav.getModel(), req, resp);
    }
}
