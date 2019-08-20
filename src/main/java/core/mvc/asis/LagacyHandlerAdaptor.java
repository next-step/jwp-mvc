package core.mvc.asis;

import core.mvc.HandlerAdaptor;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LagacyHandlerAdaptor implements HandlerAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(LagacyHandlerAdaptor.class);

    @Override
    public boolean isSupport(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        String viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
