package core.mvc.asis;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.ModelAndView;
import core.mvc.tobe.HandlerAdapter;
import core.mvc.tobe.HandlerAdapters;
import core.mvc.tobe.HandlerMappings;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final Object HANDLER_MAPPING_BASE_PACKAGE = "next.controller";
    private static final Object HANDLER_ADAPTER_BASE_PACKAGE = "core.mvc.tobe";

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    @Override
    public void init() throws ServletException {
        handlerMappings = new HandlerMappings(HANDLER_MAPPING_BASE_PACKAGE);
        handlerAdapters = new HandlerAdapters(HANDLER_ADAPTER_BASE_PACKAGE);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            handleRequest(request, response);
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<Object> maybeHandler = handlerMappings.getHandler(request);
        if (maybeHandler.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Object handler = maybeHandler.get();
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        ModelAndView mav = handlerAdapter.handle(request, response, handler);
        mav.render(request, response);
    }
}
