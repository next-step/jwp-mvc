package core.mvc.asis;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.tobe.AnnotationHandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerAdapters;
import core.mvc.tobe.HandlerMappings;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters adapters;

    @Override
    public void init() throws ServletException {
        this.handlerMappings = new HandlerMappings(List.of(new LegacyHandlerMapping(), new AnnotationHandlerMapping()));
        this.adapters = new HandlerAdapters(List.of(new LegacyHandlerAdapterAdapter(), new AnnotationHandlerAdapter()));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            var handler = handlerMappings.getHandler(req);
            var handlerAdapter = adapters.findAdapter(handler, requestUri);

            var modelAndView = handlerAdapter.handle(req, resp, handler);
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
