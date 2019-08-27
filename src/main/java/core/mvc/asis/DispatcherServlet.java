package core.mvc.asis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.exceptions.HandlerMappingException;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.handler.ControllerHandlerAdapter;
import core.handler.ExecutionHandlerAdapter;
import core.handler.HandlerAdapter;
import core.mvc.tobe.AnnotationHandlerMapping;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_BASE_PACKAGE = "next.controller";

    private List<HandlerMapping<?>> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private String basePackage;

    public DispatcherServlet() {
        this(DEFAULT_BASE_PACKAGE);
    }

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() throws ServletException {

        LegacyHandlerMapping rm = new LegacyHandlerMapping();
        rm.initMapping();
        handlerMappings.add(rm);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new ExecutionHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
        	Object handler = getMatchedHandler(req, resp);
            HandlerAdapter handlerAdapter = getMatchedHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(req, resp, handler);
            viewRender(modelAndView, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
    
    private Object getMatchedHandler(HttpServletRequest req, HttpServletResponse resp) {
    	return handlerMappings.stream()
    			.filter(handlerMapping -> handlerMapping.support(req))
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .findFirst()
                .orElseThrow(() -> new HandlerMappingException("not found handler mapping"));
    }
    
    private HandlerAdapter getMatchedHandlerAdapter(Object handler) {
    	return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new HandlerMappingException("not found handler adapter"));
    }

    private void viewRender(ModelAndView modelAndView, HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	if (modelAndView.getView() == null) {
            return;
        }
        modelAndView.getView().render(modelAndView.getModel(), req, resp);
    }
}
