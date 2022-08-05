package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.adapter.AnnotationHandlerAdapter;
import core.mvc.tobe.adapter.HandlerAdapter;
import core.mvc.tobe.adapter.NotExistAdapterException;
import core.mvc.tobe.adapter.RequestMappingAdapter;
import core.mvc.tobe.handler.AnnotationHandlerMapping;
import core.mvc.tobe.handler.HandlerMapping;
import core.mvc.tobe.handler.NotExistHandlerException;
import core.mvc.tobe.handler.RequestMapping;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String BASE_PACKAGE_START = "next";

    List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new RequestMappingAdapter());

        handlerMappings.add(new AnnotationHandlerMapping());
        handlerMappings.add(new RequestMapping());
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
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }

        throw new NotExistAdapterException("요청에 맞는 어뎁터가 없습니다.");
    }

    private Object findHandler(HttpServletRequest req) throws NotFoundException {
        for (HandlerMapping handlerMapping : handlerMappings) {
            return handlerMapping.getHandler(req);
        }

        throw new NotExistHandlerException("요청에 맞는 핸들러가 없습니다.");
    }
}
