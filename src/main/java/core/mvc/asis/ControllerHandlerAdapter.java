package core.mvc.asis;

import core.mvc.Controller;
import core.mvc.DefaultView;
import core.mvc.HandlerAdapter;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ControllerHandlerAdapter.class);

    @Override
    public boolean getHandlerAdapter(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (handler == null) {
            logger.debug("handler null");
        }
        final String viewName = ((Controller) handler).execute(request, response);
        logger.debug("controller handler adapter - viewName: {}", viewName);
        return new ModelAndView(new DefaultView(viewName));
    }
}
