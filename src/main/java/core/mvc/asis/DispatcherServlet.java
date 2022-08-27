package core.mvc.asis;

import core.annotation.web.RequestMethod;
import core.configuration.ApplicationContext;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerKey;
import core.web.view.ModelAndView;
import next.support.resolver.ArgumentResolver;
import next.support.resolver.HandlerSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init() throws ServletException {
        ApplicationContext.getInstance().init();
        AnnotationHandlerMapping.getInstance().init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.getRequestMethod(req.getMethod()));
        Object handler = this.findHandler(handlerKey);
        HandlerSpec handlerSpec = new HandlerSpec(handler, handlerKey, req, resp);
        try {
            ModelAndView modelAndView = ArgumentResolver.getInstance().invokeHandler(handlerSpec);
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HandlerKey handlerKey) throws ServletException {
        Object handler = AnnotationHandlerMapping.getInstance().getHandler(handlerKey);
        if (Objects.isNull(handler)) {
            throw new ServletException();
        }

        return handler;
    }

}
