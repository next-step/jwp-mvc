package core.mvc;

import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerAdapterFactory;
import core.mvc.tobe.view.View;
import core.mvc.tobe.view.ViewResolverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends FrameworkServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdapterFactory handlerAdapterFactory;
    private ViewResolverManager viewResolverManager;

    @Override
    protected void initStrategies(ApplicationContext context) {
        handlerAdapterFactory = context.getHandlerAdapterFactory();
        viewResolverManager = context.getViewResolverManager();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            doService(req, resp);
        } catch (Throwable e) {
            logger.error("## Exception: {}", e.getMessage());
            throw new ServletException(e);
        }
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerAdapter handler = handlerAdapterFactory.getHandler(req, resp);

        if (handler == null) {
            notFoundHandler(req, resp);
            return;
        }

        ModelAndView mav = handler.handle(req, resp);
        View view = viewResolverManager.resolveView(mav.getViewName());
        view.render(mav.getModel(), req, resp);
    }

    private void notFoundHandler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.error("no matching handler: {}", req.getRequestURI());
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}
