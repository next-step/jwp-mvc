package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
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
    private static final String BASE_CONTROLLER_PACKAGE = "next.controller";

    private RequestMapping rm;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        rm = new RequestMapping();
        rm.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping(BASE_CONTROLLER_PACKAGE);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(requestUri);

        try {
            route(controller, req, resp);
        } catch (Exception e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void route(Controller controller, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if(controller == null) {
            routeAnnotationBaseController(req, resp);
        } else {
            routeClassBaseController(req, resp, controller);
        }
    }

    private void routeClassBaseController(HttpServletRequest req, HttpServletResponse resp, Controller controller) throws Exception {
        String viewName = controller.execute(req, resp);
        move(viewName, req, resp);
    }

    private void routeAnnotationBaseController(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerExecution handler = annotationHandlerMapping.getHandler(req);
        ModelAndView mav = handler.handle(req, resp);
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
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
