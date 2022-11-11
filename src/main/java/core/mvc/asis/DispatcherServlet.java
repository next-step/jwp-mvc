package core.mvc.asis;

import core.mvc.view.ModelAndView;
import core.mvc.view.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMappingLegacy requestMappingLegacy;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        requestMappingLegacy = new RequestMappingLegacy();
        requestMappingLegacy.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        ControllerLegacy controller = requestMappingLegacy.findController(requestUri);

        try {
            if (controller != null) {
                String viewName = controller.execute(req, resp);
                move(viewName, req, resp);
            } else {
                HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(req);
                ModelAndView modelAndView = handlerExecution.handle(req, resp);

                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), req, resp);
            }
        } catch (Throwable e) {
            logger.error("Exception: {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
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
