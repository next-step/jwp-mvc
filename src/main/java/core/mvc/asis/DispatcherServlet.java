package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.adapter.AnnotationHandlerAdapter;
import core.mvc.adapter.HandlerAdapter;
import core.mvc.adapter.HandlerAdapterRegistry;
import core.mvc.adapter.SimpleControllerHandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @Override
    public void init() {
        handlerMappingRegistry.add(new LegacyHandlerMapping());
        handlerMappingRegistry.add(new AnnotationHandlerMapping());

        handlerAdapterRegistry.add(new SimpleControllerHandlerAdapter());
        handlerAdapterRegistry.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = handlerMappingRegistry.getHandler(req);

        HandlerAdapter adapter = handlerAdapterRegistry.getAdapter(handler);

        try {
            ModelAndView mv = adapter.handle(handler, req, resp);
            View view = mv.getView();
            view.render(mv.getModel(), req, resp);
        } catch (Throwable e) {
            throw new ServletException(e.getMessage());
        }
    }
}
