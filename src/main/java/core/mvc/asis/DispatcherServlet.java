package core.mvc.asis;

import core.annotation.web.RequestMethod;
import core.configuration.ApplicationContext;
import core.mvc.HandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerKey;
import core.web.view.ModelAndView;
import core.web.view.ViewRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private RequestMapping requestMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;


    @Override
    public void init() throws ServletException {
        ApplicationContext.getInstance().init();
        requestMapping = new RequestMapping();
        requestMapping.init();

        annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.init();

        handlerMappings.add(requestMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = this.findHandler(req.getMethod(), requestUri);
        try {
            if (handler instanceof Controller) {
                String viewName = ((Controller) handler).execute(req, resp);
                ViewRenderer.getInstance().render(viewName, req, resp);
                return;
            }

            ModelAndView modelAndView = ((HandlerExecution) handler).handle(req, resp);
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(String methodType, String url) throws ServletException {
        RequestMethod requestMethod = RequestMethod.getRequestMethod(methodType);
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(handlerKey))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(ServletException::new);
    }

}
