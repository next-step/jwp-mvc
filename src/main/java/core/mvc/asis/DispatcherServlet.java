package core.mvc.asis;

import core.annotation.web.Controller;
import core.di.factory.BeanFactory;
import core.mvc.tobe.AnnotationHandlerMapping;
import org.reflections.Reflections;
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
    private static final String REGISTERED_BASE_PACKAGE = "next";

    private AnnotationHandlerMapping handlerMapping;

    @Override
    public void init() {
        BeanFactory beanFactory = new BeanFactory(new Reflections(REGISTERED_BASE_PACKAGE).getTypesAnnotatedWith(Controller.class));
        beanFactory.initialize();
        handlerMapping = new AnnotationHandlerMapping(beanFactory, REGISTERED_BASE_PACKAGE);
        handlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            handlerMapping.getHandler(req)
                    .handle(req, resp)
                    .viewRender(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
