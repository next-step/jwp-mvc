package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.tobe.HandlerExecution;
import next.controller.asis.LogoutController;
import next.controller.asis.ProfileController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, Controller> mappings = new HashMap<>();

    public LegacyHandlerMapping() {
        mappings.put("/users/profile", new ProfileController());
        mappings.put("/users/logout", new LogoutController());
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
    public HandlerExecution getHandler(HttpServletRequest request) {
        Controller controller = mappings.get(request.getRequestURI());
        if (controller == null) {
            return null;
        }
        try {
            Class<?> clazz = controller.getClass();
            return new HandlerExecution(clazz.newInstance(), clazz.getMethods()[0]);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
