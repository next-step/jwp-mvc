package core.mvc.asis;

import core.mvc.tobe.HandlerMapping;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestMappingLegacy implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, ControllerLegacy> mappings = new HashMap<>();

    void initMapping() {
        mappings.put("/users/login", new LoginController());
        mappings.put("/users/profile", new ProfileController());
        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/create", new CreateUserController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    @Override
    public ControllerLegacy getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return mappings.get(uri);
    }

    void put(String url, ControllerLegacy controllerLegacy) {
        mappings.put(url, controllerLegacy);
    }
}
