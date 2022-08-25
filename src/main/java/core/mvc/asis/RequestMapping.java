package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.tobe.HandlerKey;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, Controller> mappings = new HashMap<>();

    public Controller findController(String url) {
        return mappings.get(url);
    }

    @Override
    public void init() {
        // mappings.put("/", new LegacyHomeController());
        // mappings.put("/users/form", new LegacyForwardController("/user/form.jsp"));
        // mappings.put("/users/loginForm", new LegacyForwardController("/user/login.jsp"));
        // mappings.put("/users", new LegacyListUserController());
        // mappings.put("/users/login", new LegacyLoginController());
        // mappings.put("/users/profile", new LegacyProfileController());
        // mappings.put("/users/logout", new LegacyLogoutController());
        // mappings.put("/users/create", new LogoutCreateUserController());
        // mappings.put("/users/updateForm", new LegacyUpdateFormUserController());
        // mappings.put("/users/update", new LegacyUpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }

    @Override
    public Object getHandler(HandlerKey handlerKey) {
        String url = handlerKey.getUrl();
        return this.findController(url);
    }

}
