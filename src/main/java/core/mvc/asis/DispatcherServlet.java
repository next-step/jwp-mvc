package core.mvc.asis;

import com.google.common.collect.Sets;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerAdapters;
import core.mvc.tobe.HandlerMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final String NEXT_CONTROLLER_PACKAGE = "next.controller";

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    @Override
    public void init() throws ServletException {
        handlerMappings = new HandlerMappings(Sets.newHashSet(
                new RequestMapping(),
                new AnnotationHandlerMapping(NEXT_CONTROLLER_PACKAGE)
        ));

        handlerAdapters = new HandlerAdapters(Sets.newHashSet(
                new ControllerHandlerAdapter(),
                new AnnotationHandlerAdapter()
        ));
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            final Object handler = handlerMappings.get(request);
            final ModelAndView modelAndView = handlerAdapters.execute(handler, request, response);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
