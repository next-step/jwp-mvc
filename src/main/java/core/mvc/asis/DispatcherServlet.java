package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.ProxyHandlerMapping;
import core.mvc.view.ModelAndView;
import core.mvc.view.View;
import core.mvc.view.ViewResolver;
import core.mvc.view.ViewResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;

    @Override
    public void init() throws ServletException {
        handlerMapping = new ProxyHandlerMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            final Object handler = handlerMapping.getHandler(req);
            final ViewResolver viewResolver = ViewResolverFactory.of(handler, req, resp);
            final ModelAndView mv = viewResolver.resolve(req, resp);
            final View view = mv.getView();
            view.render(mv.getModel(), req, resp);  // TODO: 모델을 애초에 뷰한테 넘겨주면 좋지 않을까?
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}
