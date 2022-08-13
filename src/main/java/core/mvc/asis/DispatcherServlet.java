package core.mvc.asis;

import core.mvc.tobe.ControllerScanner;
import core.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import core.mvc.tobe.handler.adapter.ControllerHandlerAdapter;
import core.mvc.tobe.handler.adapter.HandlerAdapters;
import core.mvc.tobe.handler.mapping.AnnotationHandlerMapping;
import core.mvc.tobe.handler.mapping.HandlerMappings;
import core.mvc.tobe.handler.mapping.ManualHandlerMapping;
import core.mvc.tobe.handler.mapping.NoExistsHandlerException;
import core.mvc.tobe.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;


    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initMapping();

        ControllerScanner controllerScanner = new ControllerScanner("next.controller");
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(controllerScanner);
        annotationHandlerMapping.initialize();

        handlerMappings = new HandlerMappings(
                manualHandlerMapping,
                annotationHandlerMapping
        );
    }

    private void initHandlerAdapters() {
        handlerAdapters = new HandlerAdapters(
                List.of(
                        new ControllerHandlerAdapter(),
                        new AnnotationHandlerAdapter()
                )
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
        try {
            Object handler = handlerMappings.getHandler(req);
            ModelAndView modelAndView = handlerAdapters.handle(handler, req, resp);
            modelAndView.doRender(req, resp);
        } catch (NoExistsHandlerException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

}
