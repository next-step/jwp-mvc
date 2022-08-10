package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.adapter.*;
import core.mvc.tobe.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdapters handlerAdapters;
    private HandlerMappings handlerMappings;

    @Override
    public void init() {
        handlerMappings = new HandlerMappings(createHandlerMappings());
        handlerAdapters = new HandlerAdapters(createHandlerAdapters());
    }

    private List<HandlerAdapter> createHandlerAdapters() {
        return Arrays.asList(
                new AnnotationHandlerAdapter(),
                new LegacyHandlerMappingAdapter()
        );
    }

    private List<HandlerMapping> createHandlerMappings() {
        return Arrays.asList(
                new AnnotationHandlerMapping(),
                new LegacyHandlerMapping()
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = null;
        try {
            handler = handlerMappings.findHandler(req);
            HandlerAdapter handlerAdapter = handlerAdapters.findHandlerAdapter(handler);
            ModelAndView mv = handlerAdapter.handle(req, resp, handler);

            mv.getView().render(mv.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
