package core.mvc.asis;

import com.google.common.collect.Lists;
import core.mvc.HandlerAdapter;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.RequestMappingHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings = Lists.newArrayList();
    private List<HandlerAdapter> handlerAdapters = Lists.newArrayList();

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Object handler = getHandler(request);

            HandlerAdapter ha = getHandlerAdapter(handler);

            ModelAndView mv = ha.handle(request, response, handler);
            View view = mv.getView();
            view.render(mv.getModel(), request, response);

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new ServletException("Mapped handler Not found"));
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("Handler adapter not found"));
    }

    private void initHandlerMappings() {
        handlerMappings.add(new RequestMapping());
        handlerMappings.add(new AnnotationHandlerMapping("next.controller"));

        // initialize
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new LegacyHandlerAdapter());
        handlerAdapters.add(new RequestMappingHandlerAdapter());
    }
}
