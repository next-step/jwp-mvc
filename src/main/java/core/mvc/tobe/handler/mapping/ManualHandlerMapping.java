package core.mvc.tobe.handler.mapping;

import core.mvc.asis.Controller;
import core.mvc.asis.DispatcherServlet;
import core.mvc.asis.ForwardController;
import core.mvc.tobe.handler.mapping.HandlerMapping;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
        logger.info("Initialized Request Mapping!");
        mappings.put("/", new HomeController());

        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    public Controller findController(String url) {
        return mappings.get(url);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return findController(request.getRequestURI());
    }
}
