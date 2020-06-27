package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.resolver.HandlerMethodArgumentResolver;
import core.mvc.resolver.HandlerMethodArgumentResolverComposite;
import core.mvc.view.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import java.io.IOException;
import java.util.Optional;
import javassist.NotFoundException;
import javax.servlet.RequestDispatcher;
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
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final View EMPTY_VIEW = (model, request, response) -> {};

    private AnnotationHandlerMapping am;
    private HandlerMethodArgumentResolver handlerMethodArgumentResolver;

    @Override
    public void init(){
        am = new AnnotationHandlerMapping(HandlerMethodArgumentResolverComposite.newInstance(),"next.controller");
        am.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            ModelAndView mav = handle(req, resp);
            mav.getView().render(mav.getModel(), req, resp);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    private ModelAndView handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return am.getHandler(req).handle(req, resp);
    }

}
