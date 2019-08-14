package core.mvc;

import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerAdapterManager;
import core.mvc.tobe.view.View;
import core.mvc.tobe.view.ViewResolverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

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

    private HandlerAdapterManager handlerAdapterManager;
    private ViewResolverManager viewResolverManager;

    @Override
    public void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Environment environment = new Environment();
        setEnvironmentInContext(environment);
        this.handlerAdapterManager = new HandlerAdapterManager(environment);
        this.viewResolverManager = new ViewResolverManager();
        stopWatch.stop();
        logger.info("dispatcherServlet initialize time: [" + stopWatch.getLastTaskTimeMillis() + "] millis");
    }

    private void setEnvironmentInContext(Environment environment) {
        if (getServletConfig() != null) {
            getServletContext().setAttribute(Environment.class.getName(), environment);
        }
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
        HandlerAdapter handler = handlerAdapterManager.getHandler(req, resp);

        if (handler == null) {
            notFoundHandler(req, resp);
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
