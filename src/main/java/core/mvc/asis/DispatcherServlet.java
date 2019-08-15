package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
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

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private List<HandlerMapping> handlerMappings;

    @Override
    public void init() throws ServletException {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(initLegacyMvcHandlerMapping());
        handlerMappings.add(initAnnotationHandlerMapping());
    }

    private LegacyMvcHandlerMapping initLegacyMvcHandlerMapping() {
        LegacyMvcHandlerMapping lmhm = new LegacyMvcHandlerMapping();
        lmhm.initialize();
        return lmhm;
    }

    private AnnotationHandlerMapping initAnnotationHandlerMapping() {
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
        return ahm;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            execute(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object handler = getHandler(req);

        if (handler instanceof Controller) {
            executeWithLegacyController(req, resp, (Controller) handler);
            return;
        }

        if (handler instanceof HandlerExecution) {
            executeWithAnnotation(req, resp, (HandlerExecution) handler);
        }
    }

    private Object getHandler(HttpServletRequest req) throws Exception {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.support(req))
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("미등록URL : " + req.getRequestURI()));
    }

    private void executeWithLegacyController(HttpServletRequest req, HttpServletResponse resp, Controller controller) throws Exception {
        String viewName = controller.execute(req, resp);
        move(viewName, req, resp);
    }

    private void executeWithAnnotation(HttpServletRequest req, HttpServletResponse resp, HandlerExecution handlerExecution) throws Exception {
        ModelAndView modelAndView = handlerExecution.handle(req, resp);
        modelAndView.viewRender(req, resp);
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
