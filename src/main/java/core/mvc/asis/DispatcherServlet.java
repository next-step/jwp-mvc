package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.adapter.AnnotationHandlerAdapter;
import core.mvc.adapter.HandlerAdapter;
import core.mvc.adapter.SimpleControllerHandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.web.exception.NotFoundHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private List<HandlerAdapter> adapterList = new ArrayList<>();

    @Override
    public void init() {
        handlerMappingRegistry.add(new LegacyHandlerMapping());
        handlerMappingRegistry.add(new AnnotationHandlerMapping());

        adapterList.add(new SimpleControllerHandlerAdapter());
        adapterList.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = handlerMappingRegistry.getHandler(req);
        HandlerAdapter adapter = adapterList.stream()
                .filter(ad -> ad.support(handler))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NotFoundHandlerException("handler 를 찾을 수 없습니다");
                });

        try {
            adapter.handle(handler, req, resp);
        } catch (Throwable e) {
            throw new ServletException(e.getMessage());
        }
    }
}
