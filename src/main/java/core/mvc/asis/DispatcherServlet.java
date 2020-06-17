package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ProxyHandlerMapping;
import core.mvc.exceptions.HandlerNotFoundException;
import core.mvc.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;
    private final List<ViewResolver> viewResolverList = Arrays
            .asList(new LegacyControllerViewResolver(), new HandlerExecutionViewResolver());

    @Override
    public void init() throws ServletException {
        handlerMapping = new ProxyHandlerMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Object handler = handlerMapping.getHandler(req);
            final ViewResolver viewResolver = findViewResolver(handler);
            final ModelAndView mv = viewResolver.resolve(handler, req, resp);
            final View view = mv.getView();
            view.render(mv.getModel(), req, resp);  // TODO: 모델을 애초에 뷰한테 넘겨주면 좋지 않을까?
        } catch (HandlerNotFoundException e) {
            logger.error(e.getMessage());
            resp.sendRedirect("/404.jsp");
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    private ViewResolver findViewResolver(Object handler) throws HandlerNotFoundException {
        return viewResolverList.stream()
                .filter(viewResolver -> viewResolver.isSupports(handler))
                .findFirst()
                .orElseThrow(() ->
                        new HandlerNotFoundException("Handler not found"));
    }
}
