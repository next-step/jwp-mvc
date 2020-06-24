package core.mvc.asis;

import core.mvc.HandlerMapping;
import next.controller.asis.LogoutController;
import next.controller.asis.UpdateFormUserController;
import next.controller.asis.UpdateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public LegacyHandlerMapping() {
        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return mappings.get(request.getRequestURI());
    }
}
