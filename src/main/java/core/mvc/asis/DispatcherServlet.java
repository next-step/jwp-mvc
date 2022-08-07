package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.adapter.*;
import core.mvc.tobe.handler.*;
import javassist.NotFoundException;
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

    private HandlerAdapters handlerAdapters;
    private HandlerMappings handlerMappings;

    @Override
    public void init() throws ServletException {
        handlerMappings = new HandlerMappings(createHandlerMappings());
        handlerAdapters = new HandlerAdapters(createHandlerAdapters());
    }

    private List<HandlerAdapter> createHandlerAdapters() {
        return Arrays.asList(
                new AnnotationHandlerAdapter(),
                new RequestMappingAdapter()
        );
    }

    private List<HandlerMapping> createHandlerMappings() {
        return Arrays.asList(
                new RequestMapping(),
                new AnnotationHandlerMapping()
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = null;
        try {
            handler = findHandler(req);
            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView mv = handlerAdapter.handle(req, resp, handler);

            mv.getView().render(mv.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws NotFoundException {
        for (HandlerAdapter handlerAdapter : handlerAdapters.getHandlerAdapters()) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }

        throw new NotExistAdapterException("요청에 맞는 어뎁터가 없습니다.");
    }

    private Object findHandler(HttpServletRequest req) throws NotFoundException {
        for (HandlerMapping handlerMapping : handlerMappings.getHandlerMappings()) {
            return handlerMapping.getHandler(req);
        }

        throw new NotExistHandlerException("요청에 맞는 핸들러가 없습니다.");
    }
}
