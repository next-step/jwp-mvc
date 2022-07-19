package core.mvc.asis;

import core.mvc.tobe.HandlerMapping;
import next.controller.UpdateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping extends HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    void initMapping() {
        mappings.put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
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
        return mappings.get(request.getRequestURI());
    }
}
