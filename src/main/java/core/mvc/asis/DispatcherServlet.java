package core.mvc.asis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.HandlerMapping;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> mappings = new ArrayList<>();


    @Override
    public void init() {
        LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();

        mappings.add(legacyHandlerMapping);
        mappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = getHandler(req);
        try {
            handle(handler, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getHandler (HttpServletRequest request) {
        return mappings.stream()
                       .map(mapping -> mapping.getHandler(request))
                       .findFirst()
                       .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 handler 입니다."));
    }

    private void handle(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof HandlerExecution) {
            HandlerExecution handlerException = (HandlerExecution)handler;
            render(req, resp, handlerException.handle(req, resp));
        } else if (handler instanceof Controller) {
            Controller controller = (Controller)handler;
            String viewName = controller.execute(req, resp);
            render(req, resp, new ModelAndView(new JspView(viewName)));
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
