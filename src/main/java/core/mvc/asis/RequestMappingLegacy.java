package core.mvc.asis;

import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingLegacy {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, ControllerLegacy> mappings = new HashMap<>();

    void initMapping() {
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users", new ListUserController());
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

    public ControllerLegacy findController(String url) {
        return mappings.get(url);
    }

    void put(String url, ControllerLegacy controllerLegacy) {
        mappings.put(url, controllerLegacy);
    }
}
