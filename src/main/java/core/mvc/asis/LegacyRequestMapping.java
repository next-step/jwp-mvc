package core.mvc.asis;

import core.mvc.DispatcherServlet;
import core.mvc.HandlerMapping;
import next.controller.LegacyCreateUserController;
import next.controller.LegacyHomeController;
import next.controller.LegacyListUserController;
import next.controller.LegacyLoginController;
import next.controller.LegacyLogoutController;
import next.controller.LegacyProfileController;
import next.controller.LegacyUpdateFormUserController;
import next.controller.LegacyUpdateUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LegacyRequestMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public void initMapping() {
        mappings.put("/", new LegacyHomeController());
        mappings.put("/users/form", new LegacyForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new LegacyForwardController("/user/login.jsp"));
        mappings.put("/users", new LegacyListUserController());
        mappings.put("/users/login", new LegacyLoginController());
        mappings.put("/users/profile", new LegacyProfileController());
        mappings.put("/users/logout", new LegacyLogoutController());
        mappings.put("/users/create", new LegacyCreateUserController());
        mappings.put("/users/updateForm", new LegacyUpdateFormUserController());
        mappings.put("/users/update", new LegacyUpdateUserController());

        logger.info("Initialized Legacy Request Mapping!");
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
    public Controller getHandler(HttpServletRequest request) {
        return mappings.get(request.getRequestURI());
    }
}
