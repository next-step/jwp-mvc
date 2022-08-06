package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutable;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.NotFoundExecution;
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

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        HandlerExecutable handlerExecutable = getHandlerExecutable(request);

        try {
            final ModelAndView modelAndView = handlerExecutable.handle(request, response);
            modelAndView.render(request, response);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }

    }

    private HandlerExecutable getHandlerExecutable(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final HandlerExecutable handler = handlerMapping.getHandler(request);
            if (handler != null && handler.executable()) {
                return handler;
            }
        }
        return new NotFoundExecution();
    }

}
