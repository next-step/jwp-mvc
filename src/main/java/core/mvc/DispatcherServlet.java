package core.mvc;

import core.mvc.handler.HandlerExecution;
import core.mvc.handlerMapping.AnnotationHandlerMapping;
import core.mvc.handlerMapping.HandlerMapping;
import core.mvc.support.*;
import core.mvc.support.exception.HandlerNotFoundException;
import core.mvc.view.JspView;
import core.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings;
    private HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite;

    @Override
    public void init() {
        final AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(BASE_PACKAGE);
        ahm.initialize();

        handlerMappings = Arrays.asList(ahm);

        handlerMethodArgumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        handlerMethodArgumentResolverComposite.addResolver(new ServletRequestResolver());
        handlerMethodArgumentResolverComposite.addResolver(new ServletResponseResolver());
        handlerMethodArgumentResolverComposite.addResolver(new RequestParamResolver());
        handlerMethodArgumentResolverComposite.addResolver(new ModelAttributeResolver());
        handlerMethodArgumentResolverComposite.addResolver(new PathVariableResolver());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            ModelAndView mav = null;
            try {
                final Object handler = getHandler(req);
                mav = handle(handler, req, resp, handlerMethodArgumentResolverComposite);
            } catch (HandlerNotFoundException handlerNotFoundException) {
                mav = new ModelAndView(new JspView("/error/page404.jsp"));
            }
            render(mav, req, resp);
        } catch (Exception e) {
            logger.error("Exception is: ", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView handle(Object handler, HttpServletRequest req, HttpServletResponse resp, HandlerMethodArgumentResolverComposite handlerMethodArgumentResolverComposite) throws Exception {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(req, resp, handlerMethodArgumentResolverComposite);
        }
        throw new ServletException("handler not Found");
    }

    private Object getHandler(HttpServletRequest req) throws ServletException {
        return handlerMappings.stream()
                .map(hm -> hm.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerNotFoundException(req.getRequestURI(), req.getMethod()));
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        final Map<String, Object> model = mav.getModel();
        final View view = mav.getView();

        view.render(model, req, resp);
    }

}
