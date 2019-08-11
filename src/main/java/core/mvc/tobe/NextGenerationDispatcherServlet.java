package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NextGenerationDispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(NextGenerationDispatcherServlet.class);

    private static final String DEFAULT_BASE_PACKAGE = "next.controller";

    private AnnotationHandlerMapping hm;

    @Override
    public void init() {
        hm = new AnnotationHandlerMapping(DEFAULT_BASE_PACKAGE);
        hm.initialize();
    }

    @Override
    protected void service(final HttpServletRequest req,
                           final HttpServletResponse resp) throws ServletException {
        final HandlerExecution execution = hm.getHandler(req);
        try {
            final ModelAndView mav = execution.handle(req, resp);

            final View view = mav.getView();

            view.render(mav.getModel(), req, resp);
        } catch (final Throwable e) {
            logger.error("Exception ", e);
            throw new ServletException(e.getMessage());
        }
    }
}


