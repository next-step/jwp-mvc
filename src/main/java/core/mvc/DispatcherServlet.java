package core.mvc;

import core.mvc.view.JspView;
import core.mvc.view.JspViewResolver;
import core.mvc.view.View;
import core.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private AnnotationHandlerMapping annotationHandlerMapping;

    // ViewResolver는 앞으로 추가 될 가능성이 높아 List로 선언한다.
    private List<ViewResolver> viewResolvers = Collections.singletonList(new JspViewResolver());

    @Override
    public void init() throws ServletException {
        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(request);

            ModelAndView modelAndView = handlerExecution.handle(request, response);

            for (ViewResolver viewResolver : this.viewResolvers) {
                if(viewResolver.supports(JspView.class)) {
                    View view = viewResolver.resolveViewName(modelAndView.getViewName());
                    view.render(modelAndView.getModel(), request, response);
                }
            }

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
}
